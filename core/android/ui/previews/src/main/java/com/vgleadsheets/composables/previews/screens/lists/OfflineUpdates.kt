package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.jakewharton.threetenabp.AndroidThreeTen
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.list.WidthClass
import com.vgleadsheets.model.updates.OfflineJobStatus
import com.vgleadsheets.model.updates.OfflineUpdateResult
import com.vgleadsheets.remaster.offline.updates.State
import com.vgleadsheets.scaffold.currentWindowWidthClassSynthetic
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

@DevicePreviews
@Composable
internal fun OfflineUpdatesList(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    AndroidThreeTen.init(LocalContext.current)
    val screenState = offlineUpdatesContentState()
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme,
    )
}

@DevicePreviews
@Composable
internal fun OfflineUpdatesListLoading(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = State(results = LCE.Loading("fetchResults"))
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme,
    )
}

@DevicePreviews
@Composable
internal fun OfflineUpdatesListEmpty(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = State(results = LCE.Content(emptyList()))
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme,
    )
}

@DevicePreviews
@Composable
internal fun OfflineUpdatesListError(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = State(
        results = LCE.Error("fetchResults", RuntimeException("Failed to load results."))
    )
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme,
    )
}

@Suppress("MagicNumber")
private fun offlineUpdatesContentState(): State {
    val result1 = OfflineUpdateResult(
        id = 1,
        dateTime = ZonedDateTime.of(2026, 4, 23, 15, 45, 0, 0, ZoneOffset.UTC),
        serverUpdateTime = ZonedDateTime.of(2026, 4, 23, 15, 40, 0, 0, ZoneOffset.UTC),
        updatedSongs = 42,
        successfulOfflines = 38,
        status = OfflineJobStatus.COMPLETED,
    )
    val result2 = OfflineUpdateResult(
        id = 2,
        dateTime = ZonedDateTime.of(2026, 4, 22, 11, 30, 0, 0, ZoneOffset.UTC),
        serverUpdateTime = ZonedDateTime.of(2026, 4, 22, 11, 25, 0, 0, ZoneOffset.UTC),
        updatedSongs = 17,
        successfulOfflines = 0,
        status = OfflineJobStatus.ABORTED,
    )

    return State(
        results = LCE.Content(listOf(result1, result2)),
        formattedDateTimes = mapOf(
            1 to "Apr 23, 2026, 3:45:00 PM",
            2 to "Apr 22, 2026, 11:30:00 AM",
        ),
        formattedServerTimes = mapOf(
            1 to "Apr 23, 2026, 3:40:00 PM",
            2 to "Apr 22, 2026, 11:25:00 AM",
        ),
    )
}
