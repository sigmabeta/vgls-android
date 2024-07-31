package com.vgleadsheets.composables.previews.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.composables.previews.ScreenPreviewDark
import com.vgleadsheets.composables.previews.ScreenPreviewLight
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.games.detail.State
import com.vgleadsheets.urlinfo.UrlInfo
import java.util.Random

@Preview
@Composable
private fun GameDetailLight(modifier: Modifier = Modifier) {
    val screenState = gameScreenState()

    ScreenPreviewLight(screenState)
}

@Preview
@Composable
private fun GameDetailDark(modifier: Modifier = Modifier) {
    val screenState = gameScreenState()

    ScreenPreviewDark(screenState)
}

@Suppress("MagicNumber")
private fun gameScreenState(): State {
    val seed = 1234L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val game = modelGenerator.randomGame()
    val composers = modelGenerator.randomComposers()
    val songs = modelGenerator.randomSongs()

    val screenState = State(
        sheetUrlInfo = UrlInfo(partId = Part.C.apiId),
        game = game,
        composers = composers,
        songs = songs,
    )
    return screenState
}
