package com.vgleadsheets.repository

import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.network.VglsApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RealRepository constructor(
    private val vglsApi: VglsApi,
    private val database: VglsDatabase
) : Repository {

    private val gameDao = database.gameDao()
    private val songDao = database.songDao()

    var lastDbWrite = 0L

    override fun getGames(force: Boolean): Observable<Data<List<Game>>> {

        return Observable
            .merge(
                gameDao.getAll()
                    .filter { it.isNotEmpty() }
                    .map { gameEntities ->
                        gameEntities.map { gameEntity ->
                            val songs = songDao
                                .getSongsForGameSync(gameEntity.id)
                                .map { it.toSong() }
                            gameEntity.toGame(songs)
                        }
                    }
                    .map { Storage(it) },
                refreshGames(force)
            )
    }

    private fun refreshGames(force: Boolean): Observable<Data<List<Game>>> {
        return Observable.just(System.currentTimeMillis())
            .subscribeOn(Schedulers.io())
            .map { force || isDbOutdated(it) }
            .flatMap {
                if (it) {
                    Timber.v("DB outdated; ${System.currentTimeMillis() - lastDbWrite}ms old. Starting network request.")
                    vglsApi.getAllGames()
                        .doOnNext { apiGames ->
                            lastDbWrite = System.currentTimeMillis()

                            val gameEntities = apiGames.map { apiGame -> apiGame.toGameEntity() }
                            val songEntities = apiGames.flatMap { game ->
                                game.songs.map { apiSong -> apiSong.toSongEntity(game.game_id) }
                            }

                            gameDao.refreshTable(gameEntities, songDao, songEntities)
                        }
                        .map<Data<List<Game>>?> { Network() }
                        .startWith(Empty())
                } else {
                    Timber.v("DB is fine! ${System.currentTimeMillis() - lastDbWrite}ms old. Continue on.")
                    Observable.just(Network())
                }
            }

    }

    private fun isDbOutdated(currentTimeMillis: Long) = currentTimeMillis - lastDbWrite > 1000L
}