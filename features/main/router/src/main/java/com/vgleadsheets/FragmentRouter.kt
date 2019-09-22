package com.vgleadsheets

interface FragmentRouter {
    fun showGameList()
    fun showSongListForGame(gameId: Long)
    fun showSongViewer(songId: Long)
    fun showSearch()
    fun showSongListForComposer(composerId: Long)
    fun showComposerList()
    fun showAllSheets()
    fun showRandomSheet()
}
