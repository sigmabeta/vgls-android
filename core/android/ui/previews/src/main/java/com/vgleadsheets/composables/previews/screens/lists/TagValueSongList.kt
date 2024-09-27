package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.tags.songs.State
import java.util.Random

@DevicePreviews
@Composable
internal fun TagValueSongList(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = tagValueSongScreenState()

    ListScreenPreview(screenState, darkTheme = darkTheme)
}

@DevicePreviews
@Composable
internal fun TagValueSongListLoading(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = loadingScreenState()

    ListScreenPreview(screenState, darkTheme = darkTheme)
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
