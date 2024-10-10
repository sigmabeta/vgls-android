package com.vgleadsheets.tracking

import com.vgleadsheets.logging.Hatchet

@SuppressWarnings("TooManyFunctions")
class NoopTracker(
    private val hatchet: Hatchet
) : Tracker {
    override fun logRefreshClick() {
        hatchet.d("Refresh forced.")
    }

    override fun logAutoRefresh() {
        hatchet.d("Refresh performed automatically.")
    }

    override fun logStickerBr() {
        hatchet.d("Stickerbr search detected.")
    }

    override fun logSearch(query: String) {
        hatchet.d("Searching for: $query")
    }

    override fun logPartSelect(transposition: String) {
        hatchet.d("Transposition selected: $transposition")
    }

    override fun logRandomSongView(songName: String, gameName: String, transposition: String) {
        hatchet.d("Random song loaded: $gameName - $songName; for $transposition")
    }

    override fun logError(message: String) = Unit

    override fun logWebLaunch(url: String, fromScreen: TrackingScreen, fromDetails: String) {
        hatchet.d("Web browser launched with url $url from screen: $fromScreen:$fromDetails")
    }

    override fun logSearchSuccess(query: String, toScreen: TrackingScreen, toDetails: String) {
        hatchet.d("Search result clicked: $toScreen:$toDetails from query $query")
    }

    override fun logScreenView(
        screen: TrackingScreen,
        details: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        hatchet.v("Screen view: $screen:$details from screen: $fromScreen:$fromDetails")
    }

    override fun logGameView(
        gameName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        hatchet.d("Game $gameName viewed from screen: $fromScreen:$fromDetails")
    }

    override fun logComposerView(
        composerName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        hatchet.d("Composer $composerName viewed from screen: $fromScreen:$fromDetails")
    }

    override fun logSongView(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        hatchet.i(

            "Song $gameName - $songName, for $transposition; viewed from screen: $fromScreen:$fromDetails"
        )
    }

    override fun logShadowClick() {
        hatchet.d("Shadow clicked.")
    }

    override fun logSearchButtonClick() {
        hatchet.d("Search button clicked.")
    }

    override fun logAppBarButtonClick() {
        hatchet.d("App bar button clicked.")
    }

    override fun logBottomMenuButtonClick() {
        hatchet.d("Bottom Menu button clicked.")
    }

    override fun logChangePartClick() {
        hatchet.d("Change part button clicked.")
    }

    override fun logScreenLinkClick(
        screenId: String,
        fromScreen: TrackingScreen,
        trackingDetails: String
    ) {
        hatchet.d("Screen link to $screenId clicked.")
    }

    override fun logRandomClick() {
        hatchet.d("Random select button clicked.")
    }
}
