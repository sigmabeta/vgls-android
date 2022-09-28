package com.vgleadsheets.tracking

import android.app.Activity

@SuppressWarnings("TooManyFunctions")
interface Tracker {
    fun logScreenView(
        activity: Activity,
        screen: TrackingScreen,
        details: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    )

    /**
     * Screens that matter
     */

    fun logGameView(
        gameName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    )

    fun logComposerView(
        composerName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    )

    @SuppressWarnings("LongParameterList")
    fun logSongView(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    )

    fun logJamFollow(
        id: Long,
        fromScreen: TrackingScreen,
        fromDetails: String
    )

    fun logWebLaunch(
        url: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    )

    /**
     * Clicks
     */

    fun logShadowClick()

    fun logSearchButtonClick()

    fun logRefreshClick()

    fun logAppBarButtonClick()

    fun logBottomMenuButtonClick()

    fun logChangePartClick()

    fun logScreenLinkClick(screenId: String, fromScreen: TrackingScreen, trackingDetails: String)

    fun logRandomClick()

    /**
     * Misc events
     */

    fun logAutoRefresh()

    fun logSearch(query: String)
    fun logSearchSuccess(query: String, toScreen: TrackingScreen, toDetails: String)

    fun logPartSelect(transposition: String)
    fun logRandomSongView(songName: String, gameName: String, transposition: String)

    fun logStickerBr()
    fun logError(message: String)
}
