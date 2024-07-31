package com.vgleadsheets.composables.previews.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.composables.previews.ScreenPreviewDark
import com.vgleadsheets.composables.previews.ScreenPreviewLight
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.songs.detail.State
import com.vgleadsheets.urlinfo.UrlInfo
import java.util.Random

@Preview
@Composable
private fun SongDetailLight(modifier: Modifier = Modifier) {
    val screenState = songScreenState()

    ScreenPreviewLight(screenState)
}

@Preview
@Composable
private fun SongDetailDark(modifier: Modifier = Modifier) {
    val screenState = songScreenState()

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
    val tags = modelGenerator.randomTags()

    val screenState = State(
        sheetUrlInfo = UrlInfo(partId = Part.C.apiId),
        song = song,
        composers = composers,
        game = game,
        songAliases = emptyList(),
        tagValues = tags
    )
    return screenState
}
