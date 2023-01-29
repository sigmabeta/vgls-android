package com.vgleadsheets.tracking

import com.vgleadsheets.logging.Hatchet

@SuppressWarnings("TooManyFunctions")
class NoopTracker(
    private val hatchet: Hatchet
) : Tracker {
    override fun logRefreshClick() {
        hatchet.d(this.javaClass.simpleName, "Refresh forced.")
    }

    override fun logAutoRefresh() {
        hatchet.d(this.javaClass.simpleName, "Refresh performed automatically.")
    }

    override fun logStickerBr() {
        hatchet.d(this.javaClass.simpleName, "Stickerbr search detected.")
    }

    override fun logSearch(query: String) {
        hatchet.d(this.javaClass.simpleName, "Searching for: $query")
    }

    override fun logPartSelect(transposition: String) {
        hatchet.d(this.javaClass.simpleName, "Transposition selected: $transposition")
    }

    override fun logRandomSongView(songName: String, gameName: String, transposition: String) {
        hatchet.d(this.javaClass.simpleName, "Random song loaded: $gameName - $songName; for $transposition")
    }

    override fun logJamFollow(id: Long, fromScreen: TrackingScreen, fromDetails: String) {
        hatchet.d(this.javaClass.simpleName, "Jam $id followed from screen: $fromScreen:$fromDetails")
    }

    override fun logError(message: String) = Unit

    override fun logWebLaunch(url: String, fromScreen: TrackingScreen, fromDetails: String) {
        hatchet.d(this.javaClass.simpleName, "Web browser launched with url $url from screen: $fromScreen:$fromDetails")
    }

    override fun logSearchSuccess(query: String, toScreen: TrackingScreen, toDetails: String) {
        hatchet.d(this.javaClass.simpleName, "Search result clicked: $toScreen:$toDetails from query $query")
    }

    override fun logScreenView(
        screen: TrackingScreen,
        details: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        hatchet.v(this.javaClass.simpleName, "Screen view: $screen:$details from screen: $fromScreen:$fromDetails")
    }

    override fun logGameView(
        gameName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        hatchet.d(this.javaClass.simpleName, "Game $gameName viewed from screen: $fromScreen:$fromDetails")
    }

    override fun logComposerView(
        composerName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        hatchet.d(this.javaClass.simpleName, "Composer $composerName viewed from screen: $fromScreen:$fromDetails")
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
            this.javaClass.simpleName,
            "Song $gameName - $songName, for $transposition; viewed from screen: $fromScreen:$fromDetails"
        )
    }

    override fun logShadowClick() {
        hatchet.d(this.javaClass.simpleName, "Shadow clicked.")
    }

    override fun logSearchButtonClick() {
        hatchet.d(this.javaClass.simpleName, "Search button clicked.")
    }

    override fun logAppBarButtonClick() {
        hatchet.d(this.javaClass.simpleName, "App bar button clicked.")
    }

    override fun logBottomMenuButtonClick() {
        hatchet.d(this.javaClass.simpleName, "Bottom Menu button clicked.")
    }

    override fun logChangePartClick() {
        hatchet.d(this.javaClass.simpleName, "Change part button clicked.")
    }

    override fun logScreenLinkClick(
        screenId: String,
        fromScreen: TrackingScreen,
        trackingDetails: String
    ) {
        hatchet.d(this.javaClass.simpleName, "Screen link to $screenId clicked.")
    }

    override fun logRandomClick() {
        hatchet.d(this.javaClass.simpleName, "Random select button clicked.")
    }
}
