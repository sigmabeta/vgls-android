package com.vgleadsheets.composables.previews.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ScreenPreviewDark
import com.vgleadsheets.composables.previews.ScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.songs.list.State
import java.util.Random

@Preview
@Composable
private fun SongListLight(modifier: Modifier = Modifier) {
    val screenState = songScreenState()

    ScreenPreviewLight(screenState)
}

@Preview
@Composable
private fun SongListLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ScreenPreviewLight(screenState)
}

@Preview
@Composable
private fun SongListDark(modifier: Modifier = Modifier) {
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

    val songs = modelGenerator.randomSongs()

    val screenState = State(
        songs = LCE.Content(songs),
    )
    return screenState
}

private fun loadingScreenState() = State(
    songs = LCE.Loading("songs.list"),
)
