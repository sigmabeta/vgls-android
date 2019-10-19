package com.vgleadsheets.tracking

import android.app.Activity

interface Tracker {
    fun logScreenView(activity: Activity, screenName: String)
    fun logMenuShow()
    fun logForceRefresh()
    fun logSearch(query: String)
    fun logPartSelect(transposition: String)
    fun logSongView(songName: String, gameName: String, transposition: String, searchTerm: String?)
    fun logGameView(gameName: String, searchTerm: String?)
    fun logComposerView(composerName: String, searchTerm: String?)
    fun logRandomSongView(songName: String, gameName: String, transposition: String)
    fun logError(message: String)
}
