package com.vgleadsheets.analytics.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.vgleadsheets.analytics.VglsAnalytics
import kotlinx.coroutines.CoroutineScope
import net.sigmabeta.sage.android.analytics.firebase.FirebaseAnalyticsImpl
import net.sigmabeta.sage.coroutines.SageDispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VglsFirebaseAnalyticsImpl @Inject constructor(
    firebaseAnalytics: FirebaseAnalytics,
    dispatchers: SageDispatchers,
    coroutineScope: CoroutineScope,
) : FirebaseAnalyticsImpl(firebaseAnalytics, dispatchers, coroutineScope), VglsAnalytics {
    override fun logGameView(gameName: String) {
        val details = Bundle()
        details.putString(PARAM_GAME_NAME, gameName)
        logEventInBackground(EVENT_GAME_VIEW, details)
    }

    override fun logComposerView(composerName: String) {
        val details = Bundle()
        details.putString(PARAM_COMPOSER_NAME, composerName)
        logEventInBackground(EVENT_COMPOSER_VIEW, details)
    }

    override fun logSongView(id: Long, songName: String, gameName: String, transposition: String?) {
        val details = Bundle()
        details.putString(PARAM_ID, id.toString())
        details.putString(PARAM_SONG_NAME, songName)
        details.putString(PARAM_GAME_NAME, gameName)
        details.putString(PARAM_SHEET_TITLE, "$gameName|$songName")
        details.putString(PARAM_TRANSPOSITION, transposition)
        logEventInBackground(EVENT_SONG_VIEW, details)
    }

    override fun logRandomSongView(songName: String, gameName: String, transposition: String) {
        val details = Bundle()
        details.putString(PARAM_SONG_NAME, songName)
        details.putString(PARAM_GAME_NAME, gameName)
        details.putString(PARAM_TRANSPOSITION, transposition)
        logEventInBackground(EVENT_RANDOM_VIEW, details)
    }

    companion object {
        const val EVENT_SONG_VIEW = "song_view"
        const val EVENT_GAME_VIEW = "game_view"
        const val EVENT_COMPOSER_VIEW = "composer_view"
        const val EVENT_RANDOM_VIEW = "song_view_random"

        const val PARAM_GAME_NAME = "game_name"
        const val PARAM_SHEET_TITLE = "sheet_title"
        const val PARAM_ID = "id"
        const val PARAM_SONG_NAME = "song_name"
        const val PARAM_COMPOSER_NAME = "composer_name"
        const val PARAM_TRANSPOSITION = "transposition"
    }
}
