package com.vgleadsheets.repository

import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.network.VglsApi
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class RealRepository @Inject constructor(
    private val vglsApi: VglsApi,
    private val database: VglsDatabase
) : Repository {
    private val disposables = CompositeDisposable()

    override fun getGames(): Observable<List<Game>> {
        refreshGames()
        return database.gameDao().getAll()
    }

    override fun close() {
        disposables.clear()
    }

    private fun refreshGames() {
        if (isDbOutdated()) {
            val disposable = vglsApi.getAllGames()
                .subscribe(
                    { apiGames ->
                        val dbGames = apiGames.map { apiGame -> apiGame.toGame() }
                        database.gameDao().insertAll(dbGames)
                    },
                    {
                        Timber.e("Error refreshing games: ${it.message}")
                    }
                )
            disposables.add(disposable)
        }
    }

    private fun isDbOutdated() = true
}