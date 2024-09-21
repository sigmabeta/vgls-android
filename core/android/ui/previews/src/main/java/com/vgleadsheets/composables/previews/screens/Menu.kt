package com.vgleadsheets.composables.previews.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appinfo.AppInfo
import com.vgleadsheets.composables.previews.ScreenPreviewDark
import com.vgleadsheets.composables.previews.ScreenPreviewLight
import com.vgleadsheets.remaster.menu.State
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources

@Preview
@Composable
internal fun MenuScreenLight(modifier: Modifier = Modifier) {
    val stringProvider = StringResources(LocalContext.current.resources)
    val screenState = menuScreenState(stringProvider)
    ScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun MenuScreenDark(modifier: Modifier = Modifier) {
    val stringProvider = StringResources(LocalContext.current.resources)
    val screenState = menuScreenState(stringProvider)

    ScreenPreviewDark(screenState)
}

@Preview
@Composable
internal fun MenuScreenLightLoading(modifier: Modifier = Modifier) {
    val screenState = menuScreenLoadingState()
    ScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun MenuScreenDarkLoading(modifier: Modifier = Modifier) {
    val screenState = menuScreenLoadingState()

    ScreenPreviewDark(screenState)
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
