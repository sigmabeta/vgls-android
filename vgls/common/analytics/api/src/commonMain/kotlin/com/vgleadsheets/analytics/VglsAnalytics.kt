package com.vgleadsheets.analytics

import net.sigmabeta.sage.analytics.Analytics

interface VglsAnalytics : Analytics {
    fun logGameView(gameName: String)

    fun logComposerView(composerName: String)

    @Suppress("LongParameterList")
    fun logSongView(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String?,
    )

    fun logRandomSongView(songName: String, gameName: String, transposition: String)
}
