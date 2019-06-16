package com.vgleadsheets

interface FragmentRouter {
    fun showGameList()
    fun showSongListForGame(gameId: Long)
    fun showSongViewer(songId: Long)
}
