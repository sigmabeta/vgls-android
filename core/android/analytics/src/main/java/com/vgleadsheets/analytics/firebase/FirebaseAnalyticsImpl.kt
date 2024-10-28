package com.vgleadsheets.analytics.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.vgleadsheets.analytics.Analytics
import com.vgleadsheets.analytics.AnalyticsScreen
import com.vgleadsheets.analytics.getDetails
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class FirebaseAnalyticsImpl(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
) : Analytics {
    override fun logScreenView(
        action: VglsAction,
        screen: AnalyticsScreen
    ) {
        val detailsBundle = Bundle()

        detailsBundle.putString(PARAM_SCREEN, screen.toString())
        detailsBundle.putString(PARAM_SCREEN_DETAILS, action.getDetails())

        logEventInBackground(EVENT_SCREEN_VIEW, detailsBundle)
    }

    /**
     * Screens that matter
     */

    override fun logGameView(
        gameName: String,
        fromScreen: AnalyticsScreen,
        fromDetails: String
    ) {
        val details = Bundle()

        details.putString(PARAM_GAME_NAME, gameName)
        details.putString(PARAM_FROM_SCREEN, fromScreen.toString())
        details.putString(PARAM_FROM_DETAILS, fromDetails)

        logEventInBackground(EVENT_GAME_VIEW, details)
    }

    override fun logComposerView(
        composerName: String,
        fromScreen: AnalyticsScreen,
        fromDetails: String
    ) {
        val details = Bundle()

        details.putString(PARAM_COMPOSER_NAME, composerName)
        details.putString(PARAM_FROM_SCREEN, fromScreen.toString())
        details.putString(PARAM_FROM_DETAILS, fromDetails)

        logEventInBackground(EVENT_COMPOSER_VIEW, details)
    }

    override fun logSongView(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String?
    ) {
        val details = Bundle()

        details.putString(PARAM_ID, id.toString())
        details.putString(PARAM_SONG_NAME, songName)
        details.putString(PARAM_GAME_NAME, gameName)
        details.putString(PARAM_SHEET_TITLE, "$gameName|$songName")
        details.putString(PARAM_TRANSPOSITION, transposition)

        logEventInBackground(EVENT_SONG_VIEW, details)
    }

    override fun logVglsAction(action: VglsAction, fromScreen: AnalyticsScreen) {
        val detailsBundle = Bundle()

        detailsBundle.putString(PARAM_VGLS_ACTION_NAME, action.javaClass.simpleName)
        detailsBundle.putString(PARAM_FROM_SCREEN, fromScreen.toString())

        logEventInBackground(EVENT_VGLS_ACTION, detailsBundle)

    }

    override fun logVglsEvent(event: VglsEvent) {
        val detailsBundle = Bundle()

        detailsBundle.putString(PARAM_VGLS_EVENT_NAME, event.javaClass.simpleName)

        logEventInBackground(EVENT_VGLS_EVENT, detailsBundle)
    }

    /**
     * Misc events
     */

    override fun logAutoRefresh() = logEventInBackground(EVENT_AUTO_REFRESH)

    override fun logRandomSongView(songName: String, gameName: String, transposition: String) {
        val details = Bundle()
        details.putString(PARAM_SONG_NAME, songName)
        details.putString(PARAM_GAME_NAME, gameName)
        details.putString(PARAM_TRANSPOSITION, transposition)

        logEventInBackground(EVENT_RANDOM_VIEW, details)
    }

    override fun logError(message: String) {
        val details = Bundle()
        details.putString(PARAM_ERROR_MESSAGE, message)

        logEventInBackground(EVENT_APP_ERROR, details)
    }

    private fun logEventInBackground(eventName: String, detailsBundle: Bundle) {
        coroutineScope.launch(dispatchers.network) {
            firebaseAnalytics.logEvent(eventName, detailsBundle)
        }
    }

    private fun logEventInBackground(name: String) {
        coroutineScope.launch(dispatchers.network) {
            firebaseAnalytics.logEvent(name, null)
        }
    }

    companion object {
        const val EVENT_VGLS_ACTION = "vgls_action"
        const val EVENT_VGLS_EVENT = "vgls_event"
        const val EVENT_AUTO_REFRESH = "auto_refresh"
        const val EVENT_FORCE_REFRESH = "force_refresh"
        const val EVENT_SCREEN_VIEW = "screen_view_custom"
        const val EVENT_SONG_VIEW = "song_view"
        const val EVENT_GAME_VIEW = "game_view"
        const val EVENT_COMPOSER_VIEW = "composer_view"
        const val EVENT_LAUNCH_WEB = "launch_web"
        const val EVENT_RANDOM_VIEW = "song_view_random"
        const val EVENT_APP_ERROR = "error_app"

        const val PARAM_SCREEN = "screen_name"
        const val PARAM_SCREEN_DETAILS = "screen_details"
        const val PARAM_VGLS_ACTION_NAME = "action_name"
        const val PARAM_VGLS_EVENT_NAME = "event_name"
        const val PARAM_FROM_SCREEN = "from_screen"
        const val PARAM_FROM_DETAILS = "from_details"
        const val PARAM_GAME_NAME = "game_name"
        const val PARAM_SHEET_TITLE = "sheet_title"
        const val PARAM_ID = "id"
        const val PARAM_SONG_NAME = "song_name"
        const val PARAM_COMPOSER_NAME = "composer_name"
        const val PARAM_TRANSPOSITION = "transposition"
        const val PARAM_ERROR_MESSAGE = "error_message"
    }
}
