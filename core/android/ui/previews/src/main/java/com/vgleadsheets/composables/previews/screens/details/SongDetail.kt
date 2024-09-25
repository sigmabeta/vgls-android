package com.vgleadsheets.composables.previews.screens.details

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.songs.detail.State
import com.vgleadsheets.urlinfo.UrlInfo
import java.util.Random

@DevicePreviews
@Composable
internal fun SongDetail(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = songScreenState()

    ListScreenPreview(screenState, darkTheme = false)
}

@DevicePreviews
@Composable
internal fun SongDetailLoading(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = songScreenLoadingState()

    ListScreenPreview(screenState, darkTheme = false)
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
