package com.vgleadsheets.nav

import com.vgleadsheets.tracking.TrackingScreen

@Suppress("TooManyFunctions")
interface Navigator {
    fun showGameDetail(gameId: Long)

    @SuppressWarnings("LongParameterList")
    fun showSongViewer(songId: Long?)

    fun showSongDetail(songId: Long)

    fun showSearch(query: String?)

    fun showComposerDetail(composerId: Long)

    fun showSongListForTagValue(tagValueId: Long)

    fun showFavorites(
        fromScreen: TrackingScreen? = null,
        fromDetails: String? = null
    )

    fun showGameList(
        fromScreen: TrackingScreen? = null,
        fromDetails: String? = null
    )

    fun showComposerList(
        fromScreen: TrackingScreen? = null,
        fromDetails: String? = null
    )

    fun showTagList(
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

    fun searchYoutube(name: String, gameName: String)

    fun showLicenseScreen()

    fun back()

    fun setPerfSpec(specName: String)

    // TODO We should rename this interface.
    fun restartApp()

    fun goToWebUrl(url: String)

    fun toMenu()
}
