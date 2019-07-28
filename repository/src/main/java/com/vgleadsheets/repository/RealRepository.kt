package com.vgleadsheets.repository

import com.vgleadsheets.database.TableName
import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.network.VglsApi
import io.reactivex.Observable

class RealRepository constructor(
    private val vglsApi: VglsApi,
    private val database: VglsDatabase
) : Repository {

    private val gameDao = database.gameDao()
    private val songDao = database.songDao()
    private val composerDao = database.composerDao()
    private val songComposerDao = database.songComposerDao()
    private val dbStatisticsDao = database.dbStatisticsDao()

    override fun getGames(force: Boolean): Observable<Data<List<Game>>> {
        return isTableFresh(TableName.GAME, force)
            .flatMap { fresh ->
                if (!fresh) {
                    vglsApi.getAllGames()
                        .doOnNext { apiGames ->
                            val gameEntities = apiGames.map { apiGame -> apiGame.toGameEntity() }

                            val songEntities = apiGames.flatMap { game ->
                                game.songs.map { apiSong -> apiSong.toSongEntity(game.game_id) }
                            }

                            apiGames.map { game ->
                                game.songs
                                    .map { apiSong -> apiSong.tags["composer"] as String? }
                                    .filterNotNull()
                                    .map { name -> ComposerEntity(null, name) }
                            }.map { composerEntities ->
                                val ids = composerDao.insertAll(composerEntities)

                            }

                            gameDao.refreshTable(
                                gameEntities,
                                songDao,
                                composerDao,
                                songComposerDao,
                                dbStatisticsDao,
                                songEntities
                            )
                        }
                        .map<Data<List<Game>>?> { Network() }
                        .startWith(Empty())
                } else {
                    gameDao.getAll()
                        .filter { it.isNotEmpty() }
                        .map { gameEntities ->
                            gameEntities.map { gameEntity ->
                                val songs = songDao
                                    .getSongsForGameSync(gameEntity.id)
                                    .map { songEntity ->
                                        val composers =
                                            songComposerDao.getComposersForSong(songEntity.id)
                                                .map { composerEntity ->
                                                    composerEntity.toComposer()
                                                }
                                        songEntity.toSong(composers)
                                    }
                                gameEntity.toGame(songs)
                            }
                        }
                        .map { Storage(it) }
                }
            }
    }

    override fun getSongs(gameId: Long): Observable<Data<List<Song>>> = songDao
        .getSongsForGame(gameId)
        .filter { it.isNotEmpty() }
        .map { songEntities -> songEntities.map { it.toSong() } }
        .map { Storage(it) }

    override fun getSongImageUrl(songId: Long): Observable<Data<String>> = songDao
        .getSong(songId)
        .map { it.toSong(null) }
        .map { it.filename }
        .map { Storage(it) }

    override fun search(searchQuery: String): Observable<List<SearchResult>> = songDao
        .searchSongsByTitle("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { songEntities -> songEntities.map { it.toSearchResult() } }

    private fun isTableFresh(tableName: TableName, force: Boolean): Observable<Boolean> {
        return dbStatisticsDao.getLastEditDate(tableName.ordinal)
            .map { lastEdit -> force || System.currentTimeMillis() - lastEdit.last_edit_time_ms < AGE_THRESHOLD }
    }

    companion object {
        const val AGE_THRESHOLD = 60000L
    }
}
