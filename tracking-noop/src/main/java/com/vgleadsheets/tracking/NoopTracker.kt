package com.vgleadsheets.tracking

import android.app.Activity

class NoopTracker : Tracker {
    override fun logMenuShow() = Unit
    override fun logForceRefresh() = Unit
    override fun logSearch(query: String) = Unit
    override fun logPartSelect(transposition: String) = Unit
    override fun logRandomSongView(songName: String, gameName: String, transposition: String) = Unit
    override fun logError(message: String) = Unit
    override fun logAutoRefresh() = Unit
    override fun logWebLaunch(details: String, fromScreen: TrackingScreen, fromDetails: String)  = Unit
    override fun logSearchSuccess(query: String, toScreen: TrackingScreen, toDetails: String) = Unit
    override fun logStickerBr() = Unit

    override fun logScreenView(
        activity: Activity,
        screen: TrackingScreen,
        details: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    )  = Unit

    override fun logGameView(gameName: String, fromScreen: TrackingScreen, fromDetails: String) = Unit

    override fun logComposerView(
        composerName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) = Unit

    override fun logSongView(
        songName: String,
        gameName: String,
        transposition: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) = Unit
}
