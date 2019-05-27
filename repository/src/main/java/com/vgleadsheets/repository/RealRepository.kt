package com.vgleadsheets.repository

import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.network.VglsApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RealRepository constructor(
    private val vglsApi: VglsApi,
    private val database: VglsDatabase
) : Repository {

    var lastDbWrite = 0L

    override fun getGames(force: Boolean): Observable<Data<List<GameEntity>>> = Observable
        .merge(
            database.gameDao()
                .getAll()
                .filter { it.isNotEmpty() }
                .map {
                    Timber.v("Loaded ${it.size} items from storage")
                    return@map Storage(it)
                },
            refreshGames(force)
        )

    private fun refreshGames(force: Boolean): Observable<Data<List<GameEntity>>> {
        return Observable.just(System.currentTimeMillis())
            .subscribeOn(Schedulers.io())
            .map { force || isDbOutdated(it) }
            .flatMap {
                return@flatMap if (it) {
                    Timber.v("DB outdated; ${System.currentTimeMillis() - lastDbWrite}ms old. Starting network request.")
                    vglsApi.getAllGames()
                        .doOnNext { apiGames ->
                            val dbGames = apiGames.map { apiGame -> apiGame.toGame() }
                            lastDbWrite = System.currentTimeMillis()
                            database.gameDao().nukeTable()
                            database.gameDao().insertAll(dbGames)
                        }
                        .map<Data<List<GameEntity>>?> { Network() }
                        .startWith(Empty())
                } else {
                    Timber.v("DB is fine! ${System.currentTimeMillis() - lastDbWrite}ms old. Continue on.")
                    Observable.just(Network())
                }
            }

    }

    private fun isDbOutdated(currentTimeMillis: Long) = currentTimeMillis - lastDbWrite > 1000L
}