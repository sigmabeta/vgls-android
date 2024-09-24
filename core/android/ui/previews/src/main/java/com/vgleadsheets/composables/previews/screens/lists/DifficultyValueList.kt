package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ListScreenPreviewDark
import com.vgleadsheets.composables.previews.ListScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.remaster.difficulty.values.State
import java.util.Random

@Preview
@Composable
internal fun DifficultyValueListLight(modifier: Modifier = Modifier) {
    val screenState = difficultyValueScreenState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun DifficultyValueListLightLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun DifficultyValueListDark(modifier: Modifier = Modifier) {
    val screenState = difficultyValueScreenState()

    ListScreenPreviewDark(screenState)
}

@Preview
@Composable
internal fun DifficultyValueListDarkLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ListScreenPreviewDark(screenState)
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
