package com.vgleadsheets.composables.previews.screens.details

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.composers.detail.State
import java.util.Random

@DevicePreviews
@Composable
internal fun ComposerDetail(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = composerScreenState()

    ListScreenPreview(screenState, darkTheme = darkTheme)
}

@DevicePreviews
@Composable
internal fun ComposerDetailLoading(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = composerScreenLoadingState()

    ListScreenPreview(screenState, darkTheme = darkTheme)
}

@Suppress("MagicNumber")
private fun composerScreenState(): State {
    val seed = 1234L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val composer = modelGenerator.randomComposer()
    val games = modelGenerator.randomGames()
    val songs = modelGenerator.randomSongs()

    val screenState = State(
        composer = LCE.Content(composer),
        games = LCE.Content(games),
        songs = LCE.Content(songs),
        isFavorite = LCE.Content(false),
    )
    return screenState
}

@Suppress("MagicNumber")
private fun composerScreenLoadingState(): State {
    val seed = 1234L
    val random = Random(seed)
    val stringGenerator = StringGenerator(random)

    val screenState = State(
        composer = LCE.Loading(stringGenerator.generateName()),
        games = LCE.Loading(stringGenerator.generateName()),
        songs = LCE.Loading(stringGenerator.generateName()),
        isFavorite = LCE.Loading(stringGenerator.generateName()),
    )
    return screenState
}
