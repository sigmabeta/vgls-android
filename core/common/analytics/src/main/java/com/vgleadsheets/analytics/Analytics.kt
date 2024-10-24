package com.vgleadsheets.analytics

@Suppress("TooManyFunctions")
interface Analytics {
    fun logScreenView(
        screen: AnalyticsScreen,
        details: String,
        fromScreen: AnalyticsScreen,
        fromDetails: String
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
        transposition: String,
        fromScreen: AnalyticsScreen,
        fromDetails: String
    )

    fun logWebLaunch(
        url: String,
        fromScreen: AnalyticsScreen,
        fromDetails: String
    )

    /**
     * Clicks
     */

    fun logShadowClick()

    fun logSearchButtonClick()

    fun logRefreshClick()

    fun logAppBarButtonClick()

    fun logBottomMenuButtonClick()

    fun logChangePartClick()

    fun logScreenLinkClick(screenId: String, fromScreen: AnalyticsScreen, analyticsDetails: String)

    fun logRandomClick()

    /**
     * Misc events
     */

    fun logAutoRefresh()

    fun logSearch(query: String)
    fun logSearchSuccess(query: String, toScreen: AnalyticsScreen, toDetails: String)

    fun logPartSelect(transposition: String)
    fun logRandomSongView(songName: String, gameName: String, transposition: String)

    fun logStickerBr()
    fun logError(message: String)
}
