package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import net.sigmabeta.sage.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import net.sigmabeta.sage.list.WidthClass
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.offline.content.State
import com.vgleadsheets.scaffold.currentWindowWidthClassSynthetic
import java.util.Random

@DevicePreviews
@Composable
internal fun OfflineContentList(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = offlineContentScreenState()
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@DevicePreviews
@Composable
internal fun OfflineContentListLoading(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = offlineContentScreenLoadingState()
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@Suppress("MagicNumber")
private fun offlineContentScreenState(): State {
    val seed = 1234L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val songs = modelGenerator.randomSongs().take(3)
    val games = modelGenerator.randomGames().take(4)
    val composers = modelGenerator.randomComposers().take(4)

    return State(
        offlineSongs = LCE.Content(songs),
        offlineGames = LCE.Content(games),
        offlineComposers = LCE.Content(composers),
    )
}

@Suppress("MagicNumber")
private fun offlineContentScreenLoadingState(): State {
    val seed = 1234L
    val random = Random(seed)
    val stringGenerator = StringGenerator(random)

    return State(
        offlineSongs = LCE.Loading(stringGenerator.generateName()),
        offlineGames = LCE.Loading(stringGenerator.generateName()),
        offlineComposers = LCE.Loading(stringGenerator.generateName()),
    )
}
