package com.vgleadsheets.tracking

import android.app.Activity
import timber.log.Timber

@SuppressWarnings("TooManyFunctions")
class NoopTracker : Tracker {
    override fun logRefreshClick() {
        Timber.d("Refresh forced.")
    }

    override fun logAutoRefresh() {
        Timber.d("Refresh performed automatically.")
    }

    override fun logStickerBr() {
        Timber.d("Stickerbr search detected.")
    }

    override fun logSearch(query: String) {
        Timber.d("Searching for: $query")
    }

    override fun logPartSelect(transposition: String) {
        Timber.d("Transposition selected: $transposition")
    }

    override fun logRandomSongView(songName: String, gameName: String, transposition: String) {
        Timber.d("Random song loaded: $gameName - $songName; for $transposition")
    }

    override fun logJamFollow(id: Long, fromScreen: TrackingScreen, fromDetails: String) {
        Timber.d("Jam $id followed from screen: $fromScreen:$fromDetails")
    }

    override fun logError(message: String) = Unit

    override fun logWebLaunch(url: String, fromScreen: TrackingScreen, fromDetails: String) {
        Timber.d("Web browser launched with url $url from screen: $fromScreen:$fromDetails")
    }

    override fun logSearchSuccess(query: String, toScreen: TrackingScreen, toDetails: String) {
        Timber.d("Search result clicked: $toScreen:$toDetails from query $query")
    }

    override fun logScreenView(
        activity: Activity,
        screen: TrackingScreen,
        details: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        Timber.v("Screen view: $screen:$details from screen: $fromScreen:$fromDetails")
    }

    override fun logGameView(
        gameName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        Timber.d("Game $gameName viewed from screen: $fromScreen:$fromDetails")
    }

    override fun logComposerView(
        composerName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        Timber.d("Composer $composerName viewed from screen: $fromScreen:$fromDetails")
    }

    override fun logSongView(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        Timber.i("Song $gameName - $songName, for $transposition; viewed from screen: $fromScreen:$fromDetails")
    }

    override fun logShadowClick() {
        Timber.d("Shadow clicked.")
    }

    override fun logSearchBoxClick() {
        Timber.d("Search box clicked.")
    }

    override fun logAppBarButtonClick() {
        Timber.d("App bar button clicked.")
    }

    override fun logBottomMenuButtonClick() {
        Timber.d("Bottom Menu button clicked.")
    }

    override fun logChangePartClick() {
        Timber.d("Change part button clicked.")
    }

    override fun logScreenLinkClick(
        screenId: String,
        fromScreen: TrackingScreen,
        trackingDetails: String
    ) {
        Timber.d("Screen link to $screenId clicked.")
    }

    override fun logRandomClick() {
        Timber.d("Random select button clicked.")
    }
}
