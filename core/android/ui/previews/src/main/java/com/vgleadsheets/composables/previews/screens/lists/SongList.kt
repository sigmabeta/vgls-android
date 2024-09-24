package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ListScreenPreviewDark
import com.vgleadsheets.composables.previews.ListScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.songs.list.State
import java.util.Random

@Preview
@Composable
internal fun SongListLight(modifier: Modifier = Modifier) {
    val screenState = songScreenState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun SongListLightLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun SongListDark(modifier: Modifier = Modifier) {
    val screenState = songScreenState()

    ListScreenPreviewDark(screenState)
}

@Preview
@Composable
internal fun SongListDarkLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ListScreenPreviewDark(screenState)
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
