package com.vgleadsheets.tracking.firebase

import android.app.Activity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.vgleadsheets.tracking.Tracker
import timber.log.Timber

@SuppressWarnings("TooManyFunctions")
class FirebaseTracker(val firebaseAnalytics: FirebaseAnalytics) : Tracker {

    override fun logScreenView(activity: Activity, screenName: String) {
        Timber.d("Logging screenview: $screenName")
        firebaseAnalytics.setCurrentScreen(activity, screenName, screenName.substringBefore(":"))
    }

    override fun logMenuShow() = logEvent(EVENT_SHOW_MENU)

    override fun logForceRefresh() = logEvent(EVENT_FORCE_REFRESH)

    override fun logPartSelect(transposition: String) {
        Timber.d("Logging $EVENT_PART_SELECT $transposition")

        val details = Bundle()
        details.putString(PARAM_TRANSPOSITION, transposition)

        firebaseAnalytics.logEvent(EVENT_PART_SELECT, details)
    }

    override fun logSongView(
        songName: String,
        gameName: String,
        transposition: String,
        searchTerm: String?
    ) {
        Timber.d("Logging $EVENT_SONG_VIEW $songName $gameName $transposition $searchTerm")

        val details = Bundle()
        details.putString(PARAM_SONG_NAME, songName)
        details.putString(PARAM_GAME_NAME, gameName)
        details.putString(PARAM_PAGE_TITLE, "$gameName - $songName")
        details.putString(PARAM_PAGE_TITLE_TRANSPOSITION, "$gameName - $songName: $transposition")
        details.putString(PARAM_TRANSPOSITION, transposition)
        details.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm)

        firebaseAnalytics.logEvent(EVENT_SONG_VIEW, details)
    }

    override fun logGameView(gameName: String, searchTerm: String?) {
        Timber.d("Logging $EVENT_GAME_VIEW $gameName $searchTerm")

        val details = Bundle()
        details.putString(PARAM_GAME_NAME, gameName)
        details.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm)

        firebaseAnalytics.logEvent(EVENT_GAME_VIEW, details)
    }

    override fun logComposerView(composerName: String, searchTerm: String?) {
        Timber.d("Logging $EVENT_COMPOSER_VIEW $composerName $searchTerm")

        val details = Bundle()
        details.putString(PARAM_COMPOSER_NAME, composerName)
        details.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm)

        firebaseAnalytics.logEvent(EVENT_COMPOSER_VIEW, details)
    }

    override fun logRandomSongView(songName: String, gameName: String, transposition: String) {
        Timber.d("Logging $EVENT_RANDOM_VIEW $songName $gameName $transposition")

        val details = Bundle()
        details.putString(PARAM_SONG_NAME, songName)
        details.putString(PARAM_GAME_NAME, gameName)
        details.putString(PARAM_TRANSPOSITION, transposition)

        firebaseAnalytics.logEvent(EVENT_RANDOM_VIEW, details)
    }

    override fun logSearch(query: String) {
        Timber.d("Logging search: $query")

        val details = Bundle()
        details.putString(FirebaseAnalytics.Param.SEARCH_TERM, query)

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, details)
    }

    override fun logError(message: String) {
        Timber.d("Logging $EVENT_APP_ERROR")

        val details = Bundle()
        details.putString(PARAM_ERROR_MESSAGE, message)

        firebaseAnalytics.logEvent(EVENT_APP_ERROR, details)
    }

    private fun logEvent(name: String) {
        Timber.d("Logging $name")
        firebaseAnalytics.logEvent(name, null)
    }

    companion object {
        const val EVENT_SHOW_MENU = "show_menu"
        const val EVENT_FORCE_REFRESH = "force_refresh"
        const val EVENT_SONG_VIEW = "song_view"
        const val EVENT_GAME_VIEW = "game_view"
        const val EVENT_COMPOSER_VIEW = "composer_view"
        const val EVENT_RANDOM_VIEW = "song_view_random"
        const val EVENT_PART_SELECT = "part_select"
        const val EVENT_APP_ERROR = "error_app"

        const val PARAM_GAME_NAME = "game_name"
        const val PARAM_PAGE_TITLE = "page_title"
        const val PARAM_PAGE_TITLE_TRANSPOSITION = "page_title_transposition"
        const val PARAM_SONG_NAME = "song_name"
        const val PARAM_COMPOSER_NAME = "composer_name"
        const val PARAM_TRANSPOSITION = "transposition"
        const val PARAM_ERROR_MESSAGE = "error_message"
    }
}
