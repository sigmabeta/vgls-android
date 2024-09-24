package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ListScreenPreviewDark
import com.vgleadsheets.composables.previews.ListScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.tags.songs.State
import java.util.Random

@Preview
@Composable
internal fun TagValueSongListLight(modifier: Modifier = Modifier) {
    val screenState = tagValueSongScreenState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun TagValueSongListLightLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun TagValueSongListDark(modifier: Modifier = Modifier) {
    val screenState = tagValueSongScreenState()

    ListScreenPreviewDark(screenState)
}

@Preview
@Composable
internal fun TagValueSongListDarkLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ListScreenPreviewDark(screenState)
}

@Suppress("MagicNumber")
private fun tagValueSongScreenState(): State {
    val seed = 12345L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val tagValueSongs = modelGenerator
        .randomSongs()

    val screenState = State(
        songs = LCE.Content(tagValueSongs),
    )
    return screenState
}

private fun loadingScreenState() = State(
    songs = LCE.Loading("tagValueSongs.list"),
)
