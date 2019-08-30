package com.vgleadsheets

import io.reactivex.Observable

interface FragmentRouter {
    fun showGameList()
    fun showSongListForGame(gameId: Long)
    fun showSongViewer(songId: Long)
    fun showSearch()
    fun searchClicks(): Observable<Unit>
    fun searchEvents(): Observable<String>
    fun hideSearch()
    fun popBackStack()
}
