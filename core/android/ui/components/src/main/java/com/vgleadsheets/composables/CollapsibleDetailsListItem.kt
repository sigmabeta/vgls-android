package com.vgleadsheets.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.FullScreenOf
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.id
import java.util.Random
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CollapsibleDetailsListItem(
    title: String,
    detailItems: ImmutableList<String>,
    initiallyCollapsed: Boolean = false,
    padding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        var collapsed by remember { mutableStateOf(initiallyCollapsed) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { collapsed = !collapsed }
                .padding(padding),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .weight(1.0f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            val icon = if (collapsed) Icon.PLUS else Icon.MINUS

            Icon(
                painter = painterResource(id = icon.id()),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

        detailItems.forEach { item ->
            AnimatedVisibility(!collapsed) {
                DetailItem(
                    detailText = item,
                    padding = padding,
                )
            }
        }
    }
}

@Composable
private fun DetailItem(
    detailText: String,
    padding: PaddingValues,
) {
    Row(
        modifier = Modifier
            .padding(padding),
    ) {
        Icon(
            painter = painterResource(id = Icon.JAM_FILLED.id()),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(end = 16.dp)
                .size(20.dp)
                .alpha(0.7f)
        )

        Text(
            text = detailText,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .weight(1f),
        )
    }
}

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

    CollapsibleDetailsListItem(
        title = stringGen.generateTitle(),
        detailItems = detailItems.toImmutableList(),
        initiallyCollapsed = detailCount % 2 == 0,
        padding = padding,
        modifier = Modifier,
    )
}

private const val RANDOMIZER_SEED = 1231L
