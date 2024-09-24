package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ListScreenPreviewDark
import com.vgleadsheets.composables.previews.ListScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.tags.values.State
import java.util.Random

@Preview
@Composable
internal fun TagValueListLight(modifier: Modifier = Modifier) {
    val screenState = tagValueScreenState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun TagValueListLightLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun TagValueListDark(modifier: Modifier = Modifier) {
    val screenState = tagValueScreenState()

    ListScreenPreviewDark(screenState)
}

@Preview
@Composable
internal fun TagValueListDarkLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ListScreenPreviewDark(screenState)
}

@Suppress("MagicNumber")
private fun tagValueScreenState(): State {
    val seed = 12345L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val tagValues = modelGenerator
        .randomTagValues()

    val screenState = State(
        tagValues = LCE.Content(tagValues),
    )
    return screenState
}

private fun loadingScreenState() = State(
    tagValues = LCE.Loading("tagValues.list"),
)