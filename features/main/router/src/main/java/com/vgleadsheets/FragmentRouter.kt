package com.vgleadsheets

@Suppress("TooManyFunctions")
interface FragmentRouter {
    fun showGameList()
    fun showSongListForGame(gameId: Long)
    fun showSongViewer(songId: Long)
    fun showJamViewer(jamId: Long)
    fun showSearch()
    fun showSongListForComposer(composerId: Long)
    fun showSongListForTagValue(tagValueId: Long)
    fun showComposerList()
    fun showTagList()
    fun showJams()
    fun showValueListForTagKey(tagKeyId: Long)
    fun showAllSheets()
    fun showSettings()
    fun showAbout()
    fun goToWebUrl(url: String)
    fun showLicenseScreen()
    fun showFindJamDialog()
}
