package com.vgleadsheets.tracking

import android.app.Activity

class NoopTracker : Tracker {
    override fun logScreenView(activity: Activity, screenName: String) = Unit
    override fun logMenuShow() = Unit
    override fun logForceRefresh() = Unit
    override fun logSearch(query: String) = Unit
    override fun logPartSelect(transposition: String) = Unit
    override fun logRandomSongView(songName: String, gameName: String, transposition: String) = Unit
    override fun logError(message: String) = Unit
    override fun logSongView(
        songName: String,
        gameName: String,
        transposition: String,
        searchTerm: String?
    ) = Unit
    override fun logGameView(gameName: String, searchTerm: String?) = Unit
    override fun logComposerView(composerName: String, searchTerm: String?) = Unit
}
