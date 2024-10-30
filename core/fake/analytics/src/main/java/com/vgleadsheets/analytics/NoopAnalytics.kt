package com.vgleadsheets.analytics

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.logging.Hatchet

@Suppress("TooManyFunctions")
class NoopAnalytics(
    private val hatchet: Hatchet
) : Analytics {
    override fun logAutoRefresh() {
        hatchet.d("Refresh performed automatically.")
    }

    override fun logRandomSongView(songName: String, gameName: String, transposition: String) {
        hatchet.d("Random song loaded: $gameName - $songName; for $transposition")
    }

    override fun logError(failedOperationName: String, errorString: String, error: Throwable) {
        hatchet.e("Logging error to analytics: $failedOperationName | $errorString | $error")
    }

    override fun logScreenView(
        action: VglsAction,
        screen: AnalyticsScreen
    ) {
        hatchet.v("Screen view: $screen:${action.getDetails()}")
    }

    override fun logGameView(
        gameName: String
    ) {
        hatchet.d("Game $gameName viewed")
    }

    override fun logComposerView(
        composerName: String
    ) {
        hatchet.d("Composer $composerName viewed")
    }

    override fun logSongView(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String?
    ) {
        hatchet.i("Song $gameName - $songName, for $transposition")
    }

    override fun logVglsAction(action: VglsAction, fromScreen: AnalyticsScreen) {
        hatchet.d("Logging Action $action")
    }

    override fun logVglsEvent(event: VglsEvent) {
        hatchet.d("Logging Event $event")
    }
}
