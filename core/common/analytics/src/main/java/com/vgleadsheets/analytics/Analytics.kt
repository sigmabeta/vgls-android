package com.vgleadsheets.analytics

import net.sigmabeta.sage.appcomm.VglsAction
import net.sigmabeta.sage.appcomm.VglsEvent

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
    )

    fun logComposerView(
        composerName: String,
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

    fun logError(
        failedOperationName: String,
        errorString: String,
        error: Throwable,
    )
}
