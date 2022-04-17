package com.vgleadsheets.tracking.firebase

import android.app.Activity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen

@SuppressWarnings("TooManyFunctions")
class FirebaseTracker(private val firebaseAnalytics: FirebaseAnalytics) : Tracker {

    override fun logScreenView(
        activity: Activity,
        screen: TrackingScreen,
        details: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        val detailsBundle = Bundle()

        detailsBundle.putString(PARAM_SCREEN, screen.toString())
        detailsBundle.putString(PARAM_SCREEN_DETAILS, details)
        detailsBundle.putString(PARAM_FROM_SCREEN, fromScreen.toString())
        detailsBundle.putString(PARAM_FROM_DETAILS, fromDetails)

        firebaseAnalytics.logEvent(EVENT_SCREEN_VIEW, detailsBundle)
    }

    override fun logGameView(
        gameName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        val details = Bundle()

        details.putString(PARAM_GAME_NAME, gameName)
        details.putString(PARAM_FROM_SCREEN, fromScreen.toString())
        details.putString(PARAM_FROM_DETAILS, fromDetails)

        firebaseAnalytics.logEvent(EVENT_GAME_VIEW, details)
    }

    override fun logComposerView(
        composerName: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        val details = Bundle()

        details.putString(PARAM_COMPOSER_NAME, composerName)
        details.putString(PARAM_FROM_SCREEN, fromScreen.toString())
        details.putString(PARAM_FROM_DETAILS, fromDetails)

        firebaseAnalytics.logEvent(EVENT_COMPOSER_VIEW, details)
    }

    override fun logSongView(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        val details = Bundle()

        details.putString(PARAM_ID, id.toString())
        details.putString(PARAM_SONG_NAME, songName)
        details.putString(PARAM_GAME_NAME, gameName)
        details.putString(PARAM_SHEET_TITLE, "$gameName|$songName")
        details.putString(PARAM_TRANSPOSITION, transposition)
        details.putString(PARAM_FROM_SCREEN, fromScreen.toString())
        details.putString(PARAM_FROM_DETAILS, fromDetails)

        firebaseAnalytics.logEvent(EVENT_SONG_VIEW, details)
    }

    override fun logJamFollow(id: Long, fromScreen: TrackingScreen, fromDetails: String) {
        val details = Bundle()

        details.putString(PARAM_ID, id.toString())
        details.putString(PARAM_FROM_SCREEN, fromScreen.toString())
        details.putString(PARAM_FROM_DETAILS, fromDetails)

        firebaseAnalytics.logEvent(EVENT_JAM_FOLLOW, details)
    }

    override fun logWebLaunch(
        url: String,
        fromScreen: TrackingScreen,
        fromDetails: String
    ) {
        val detailsBundle = Bundle()

        detailsBundle.putString(PARAM_SCREEN_DETAILS, url)
        detailsBundle.putString(PARAM_FROM_SCREEN, fromScreen.toString())
        detailsBundle.putString(PARAM_FROM_DETAILS, fromDetails)

        firebaseAnalytics.logEvent(EVENT_LAUNCH_WEB, detailsBundle)
    }

    override fun logMenuShow() = logEvent(EVENT_SHOW_MENU)

    override fun logAutoRefresh() = logEvent(EVENT_AUTO_REFRESH)

    override fun logForceRefresh() = logEvent(EVENT_FORCE_REFRESH)

    override fun logSearch(query: String) {
        val details = Bundle()
        details.putString(FirebaseAnalytics.Param.SEARCH_TERM, query)

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, details)
    }

    override fun logSearchSuccess(query: String, toScreen: TrackingScreen, toDetails: String) {
        val details = Bundle()

        details.putString(FirebaseAnalytics.Param.SEARCH_TERM, query)
        details.putString(PARAM_TO_SCREEN, toScreen.toString())
        details.putString(PARAM_TO_DETAILS, toDetails)

        firebaseAnalytics.logEvent(EVENT_SEARCH_SUCCESS, details)
    }

    override fun logPartSelect(transposition: String) {
        val details = Bundle()
        details.putString(PARAM_TRANSPOSITION, transposition)

        firebaseAnalytics.logEvent(EVENT_PART_SELECT, details)
    }

    override fun logRandomSongView(songName: String, gameName: String, transposition: String) {
        val details = Bundle()
        details.putString(PARAM_SONG_NAME, songName)
        details.putString(PARAM_GAME_NAME, gameName)
        details.putString(PARAM_TRANSPOSITION, transposition)

        firebaseAnalytics.logEvent(EVENT_RANDOM_VIEW, details)
    }

    override fun logStickerBr() {
        logEvent(EVENT_STICKERBR)
    }

    override fun logError(message: String) {
        val details = Bundle()
        details.putString(PARAM_ERROR_MESSAGE, message)

        firebaseAnalytics.logEvent(EVENT_APP_ERROR, details)
    }

    private fun logEvent(name: String) {
        firebaseAnalytics.logEvent(name, null)
    }

    companion object {
        const val EVENT_SHOW_MENU = "show_menu"
        const val EVENT_AUTO_REFRESH = "auto_refresh"
        const val EVENT_FORCE_REFRESH = "force_refresh"
        const val EVENT_SCREEN_VIEW = "screen_view_custom"
        const val EVENT_SONG_VIEW = "song_view"
        const val EVENT_JAM_FOLLOW = "jam_view"
        const val EVENT_GAME_VIEW = "game_view"
        const val EVENT_COMPOSER_VIEW = "composer_view"
        const val EVENT_LAUNCH_WEB = "launch_web"
        const val EVENT_SEARCH_SUCCESS = "search_success"
        const val EVENT_RANDOM_VIEW = "song_view_random"
        const val EVENT_PART_SELECT = "part_select"
        const val EVENT_APP_ERROR = "error_app"
        const val EVENT_STICKERBR = "stickerbr"

        const val PARAM_SCREEN = "screen_name"
        const val PARAM_SCREEN_DETAILS = "screen_details"
        const val PARAM_FROM_SCREEN = "from_screen"
        const val PARAM_FROM_DETAILS = "from_details"
        const val PARAM_TO_SCREEN = "to_screen"
        const val PARAM_TO_DETAILS = "to_details"
        const val PARAM_GAME_NAME = "game_name"
        const val PARAM_SHEET_TITLE = "sheet_title"
        const val PARAM_ID = "id"
        const val PARAM_SONG_NAME = "song_name"
        const val PARAM_COMPOSER_NAME = "composer_name"
        const val PARAM_TRANSPOSITION = "transposition"
        const val PARAM_ERROR_MESSAGE = "error_message"
    }
}
