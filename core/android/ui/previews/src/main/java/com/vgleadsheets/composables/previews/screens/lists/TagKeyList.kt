package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ListScreenPreviewDark
import com.vgleadsheets.composables.previews.ListScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.tags.list.State
import java.util.Random

@Preview
@Composable
internal fun TagKeyListLight(modifier: Modifier = Modifier) {
    val screenState = tagKeyScreenState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun TagKeyListLightLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun TagKeyListDark(modifier: Modifier = Modifier) {
    val screenState = tagKeyScreenState()

    ListScreenPreviewDark(screenState)
}

@Preview
@Composable
internal fun TagKeyListDarkLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ListScreenPreviewDark(screenState)
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
