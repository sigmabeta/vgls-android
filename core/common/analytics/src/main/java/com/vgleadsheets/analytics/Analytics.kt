package com.vgleadsheets.analytics

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent

@Suppress("TooManyFunctions")
interface Analytics {
    fun logScreenView(
        action: VglsAction,
        screen: AnalyticsScreen,
    )

    /**
     * Screens that matter
     */

    fun logGameView(
        gameName: String,
        fromScreen: AnalyticsScreen,
        fromDetails: String
    )

    fun logComposerView(
        composerName: String,
        fromScreen: AnalyticsScreen,
        fromDetails: String
    )

    @Suppress("LongParameterList")
    fun logSongView(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String?,
    )

    fun logVglsAction(
        action: VglsAction,
        fromScreen: AnalyticsScreen,
    )

    fun logVglsEvent(
        event: VglsEvent,
    )

    /**
     * Misc events
     */

    fun logAutoRefresh()

    fun logRandomSongView(songName: String, gameName: String, transposition: String)

    fun logError(message: String)
}
