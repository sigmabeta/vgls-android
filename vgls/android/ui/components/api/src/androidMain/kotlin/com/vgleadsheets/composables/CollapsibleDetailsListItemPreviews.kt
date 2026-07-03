package com.vgleadsheets.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.sigmabeta.sage.components.CollapsibleDetailsListModel
import com.vgleadsheets.composables.previews.FullScreenOf
import com.vgleadsheets.model.generator.StringGenerator
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.vector
import java.util.Random
import kotlinx.collections.immutable.toImmutableList

@Preview
@Composable
private fun Light() {
    val randomizer = Random(RANDOMIZER_SEED)
    val stringGen = StringGenerator(randomizer)

    FullScreenOf(darkTheme = false) { paddingValues ->
        Sample(paddingValues, randomizer, stringGen)
    }
}

@Preview
@Composable
private fun Dark() {
    val randomizer = Random(RANDOMIZER_SEED)
    val stringGen = StringGenerator(randomizer)

    FullScreenOf(darkTheme = true) { paddingValues ->
        Sample(paddingValues, randomizer, stringGen)
    }
}

@Composable
@Suppress("MagicNumber")
private fun Sample(padding: PaddingValues, randomizer: Random, stringGen: StringGenerator) {
    val detailCount = randomizer.nextInt(5)
    val detailItems = List(detailCount) {
        stringGen.generateLorem()
    }

    val title = stringGen.generateTitle()

    val model = CollapsibleDetailsListModel(
        dataId = title.hashCode().toLong(),
        title = title,
        detailItems = detailItems.toImmutableList(),
        initiallyCollapsed = detailCount % 2 == 0,
    )

    CollapsibleDetailsListItem(
        model = model,
        padding = padding,
        modifier = Modifier,
    )
}

private const val RANDOMIZER_SEED = 1231L
