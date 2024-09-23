package com.vgleadsheets.composables.previews.screens.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ScreenPreviewDark
import com.vgleadsheets.composables.previews.ScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.songs.detail.State
import com.vgleadsheets.urlinfo.UrlInfo
import java.util.Random

@Preview
@Composable
internal fun SongDetailLight(modifier: Modifier = Modifier) {
    val screenState = songScreenState()

    ScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun SongDetailLoading(modifier: Modifier = Modifier) {
    val screenState = songScreenLoadingState()

    ScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun SongDetailDark(modifier: Modifier = Modifier) {
    val screenState = songScreenState()

    ScreenPreviewDark(screenState)
}

@Preview
@Composable
internal fun SongDetailLoadingDark(modifier: Modifier = Modifier) {
    val screenState = songScreenLoadingState()

    ScreenPreviewDark(screenState)
}

@Suppress("MagicNumber")
private fun songScreenState(): State {
    val seed = 12345L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val song = modelGenerator.randomSong()
    val composers = modelGenerator.randomComposers()
    val game = modelGenerator.randomGame()
    val tags = modelGenerator.randomTagValues()

    val screenState = State(
        song = LCE.Content(song),
        composers = LCE.Content(composers),
        game = LCE.Content(game),
        songAliases = LCE.Content(emptyList()),
        tagValues = LCE.Content(tags),
        sheetUrlInfo = LCE.Content(UrlInfo(partId = "C")),
        isFavorite = LCE.Content(false),
        isAltSelected = LCE.Content(false),
    )
    return screenState
}

@Suppress("MagicNumber")
private fun songScreenLoadingState(): State {
    val seed = 12345L
    val random = Random(seed)
    val stringGenerator = StringGenerator(random)

    val screenState = State(
        song = LCE.Loading(stringGenerator.generateName()),
        composers = LCE.Loading(stringGenerator.generateName()),
        game = LCE.Loading(stringGenerator.generateName()),
        songAliases = LCE.Loading(stringGenerator.generateName()),
        tagValues = LCE.Loading(stringGenerator.generateName()),
        sheetUrlInfo = LCE.Loading(stringGenerator.generateName()),
    )
    return screenState
}
