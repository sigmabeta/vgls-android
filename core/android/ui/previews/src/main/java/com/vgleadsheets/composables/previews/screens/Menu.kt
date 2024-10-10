package com.vgleadsheets.composables.previews.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.vgleadsheets.appinfo.AppInfo
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.list.WidthClass
import com.vgleadsheets.remaster.menu.State
import com.vgleadsheets.scaffold.currentWindowWidthClassSynthetic
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources

@DevicePreviews
@Composable
internal fun MenuScreen(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val stringProvider = StringResources(LocalContext.current.resources)
    val screenState = menuScreenState(stringProvider)
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@DevicePreviews
@Composable
internal fun MenuScreenLoading(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = menuScreenLoadingState()
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@Suppress("MagicNumber")
private fun menuScreenState(stringProvider: StringProvider): State {
    val screenState = State(
        keepScreenOn = true,
        appInfo = appInfo(),
        debugClickCount = 0,
        shouldShowDebug = true,
        debugShouldDelay = true,
        debugShouldShowNavSnackbars = true,
        songRecordsGenerated = 0,
        songRecordsGeneratedLegacy = 0,
        songRecordsMigrated = 0,
    )
    return screenState
}

@Suppress("MagicNumber")
private fun menuScreenLoadingState(): State {
    val screenState = State(
        songRecordsGenerated = null,
        songRecordsGeneratedLegacy = null,
        songRecordsMigrated = null,
    )
    return screenState
}

private fun appInfo() = AppInfo(
    isDebug = true,
    versionName = "2.1.1",
    versionCode = 20101,
    buildTimeMs = null,
    buildBranch = "beta"
)
