package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.remaster.difficulty.values.State
import java.util.Random

@DevicePreviews
@Composable
internal fun DifficultyValueList(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = difficultyValueScreenState()

    ListScreenPreview(screenState, darkTheme = darkTheme)
}

@DevicePreviews
@Composable
internal fun DifficultyValueListLoading(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = loadingScreenState()

    ListScreenPreview(screenState, darkTheme = darkTheme)
}

@DevicePreviews
@Composable
internal fun DifficultyValueListDark(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = difficultyValueScreenState()

    ListScreenPreview(screenState, darkTheme = darkTheme)
}

@DevicePreviews
@Composable
internal fun DifficultyValueListDarkLoading(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = loadingScreenState()

    ListScreenPreview(screenState, darkTheme = darkTheme)
}

@Suppress("MagicNumber")
private fun difficultyValueScreenState(): State {
    val seed = 12345L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val difficultyValues = List(4) { position ->
        val index = position + 1

        TagValue(
            id = index.toLong(),
            name = index.toString(),
            tagKeyId = random.nextLong(),
            tagKeyName = "Whatever",
            songs = emptyList()
        )
    }

    val screenState = State(
        difficultyValues = LCE.Content(difficultyValues),
    )
    return screenState
}

private fun loadingScreenState() = State(
    difficultyValues = LCE.Loading("difficultyValues.list"),
)
