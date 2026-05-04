package com.vgleadsheets.analytics

import net.sigmabeta.sage.analytics.AnalyticsScreenId
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent

class NoopVglsAnalytics : VglsAnalytics {
    override fun logAutoRefresh() = Unit
    override fun logError(failedOperationName: String, errorString: String, error: Throwable) = Unit
    override fun logScreenView(action: SageAction, screen: AnalyticsScreenId) = Unit
    override fun logAction(action: SageAction, fromScreen: AnalyticsScreenId) = Unit
    override fun logEvent(event: SageEvent) = Unit
    override fun logGameView(gameName: String) = Unit
    override fun logComposerView(composerName: String) = Unit
    override fun logSongView(id: Long, songName: String, gameName: String, transposition: String?) = Unit
    override fun logRandomSongView(songName: String, gameName: String, transposition: String) = Unit
}
