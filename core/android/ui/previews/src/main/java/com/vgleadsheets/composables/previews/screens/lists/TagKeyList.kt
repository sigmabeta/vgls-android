package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.tags.list.State
import java.util.Random

@DevicePreviews
@Composable
internal fun TagKeyList(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = tagKeyScreenState()

    ListScreenPreview(screenState, darkTheme = darkTheme)
}

@DevicePreviews
@Composable
internal fun TagKeyListLoading(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = loadingScreenState()

    ListScreenPreview(screenState, darkTheme = darkTheme)
}

@Suppress("MagicNumber")
private fun tagKeyScreenState(): State {
    val seed = 12345L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val tagKeys = modelGenerator
        .randomTagKeys()
        .filter { it.isDifficultyTag() == false }

    val screenState = State(
        tagKeys = LCE.Content(tagKeys),
    )
    return screenState
}

private fun loadingScreenState() = State(
    tagKeys = LCE.Loading("tagKeys.list"),
)
