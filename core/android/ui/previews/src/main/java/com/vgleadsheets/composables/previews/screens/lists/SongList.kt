package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.songs.list.State
import java.util.Random

@DevicePreviews
@Composable
internal fun SongList(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = songScreenState()

    ListScreenPreview(screenState, darkTheme = darkTheme)
}

@DevicePreviews
@Composable
internal fun SongListLoading(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = loadingScreenState()

    ListScreenPreview(screenState, darkTheme = darkTheme)
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
