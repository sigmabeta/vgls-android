package com.vgleadsheets

import com.vgleadsheets.tracking.TrackingScreen

@Suppress("TooManyFunctions")
interface FragmentRouter {
    fun showGameList(
        fromScreen: TrackingScreen? = null,
        fromDetails: String? = null
    )

    fun showSongListForGame(gameId: Long)

    @SuppressWarnings("LongParameterList")
    fun showSongViewer(songId: Long)

    fun showSheetDetail(songId: Long)

    fun showJamViewer(jamId: Long)

    fun showJamDetailViewer(jamId: Long)

    fun showSearch()

    fun showSongListForComposer(composerId: Long)

    fun showSongListForTagValue(tagValueId: Long)

    fun showComposerList(
        fromScreen: TrackingScreen? = null,
        fromDetails: String? = null
    )

    fun showTagList(
        fromScreen: TrackingScreen? = null,
        fromDetails: String? = null
    )

    fun showJams(
        fromScreen: TrackingScreen? = null,
        fromDetails: String? = null
    )

    fun showValueListForTagKey(tagKeyId: Long)

    fun showAllSheets(
        fromScreen: TrackingScreen? = null,
        fromDetails: String? = null
    )

    fun showSettings(
        fromScreen: TrackingScreen? = null,
        fromDetails: String? = null
    )

    fun showDebug(
        fromScreen: TrackingScreen? = null,
        fromDetails: String? = null
    )

    fun showAbout()

    fun goToWebUrl(url: String)

    fun showLicenseScreen()

    fun showFindJamDialog()

    fun back()

    fun setPerfSpec(specName: String)

    // TODO We should rename this interface.
    fun restartApp()
}
