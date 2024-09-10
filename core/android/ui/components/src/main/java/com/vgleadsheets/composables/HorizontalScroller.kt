package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.components.LoadingItemListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.themes.VglsMaterial
import java.util.Random
import kotlinx.collections.immutable.toImmutableList

@Composable
@Suppress("MagicNumber")
fun HorizontalScroller(
    model: HorizontalScrollerListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    LazyRow(
        contentPadding = padding,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        items(
            items = model.scrollingItems,
            key = { it.dataId },
            contentType = { it.javaClass.simpleName },
        ) {
            it.Content(
                sink = actionSink,
                mod = Modifier.animateItem(),
                pad = PaddingValues()
            )
        }
    }
}

@Preview
@Composable
private fun Light() {
    VglsMaterial {
        Sample()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    VglsMaterial {
        Sample()
    }
}

@Composable
@Suppress("MagicNumber")
private fun Sample() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        val rng = Random("HorizontalScroller".hashCode().toLong())

        val paddingModifier = PaddingValues(
            horizontal = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.margin_side)
        )
        SquareItemSection(rng, paddingModifier)
        WideItemSection(rng, paddingModifier)
        LoadingSquareItemSection(rng, paddingModifier)
        VerticalSection(rng, paddingModifier)
        SquareItemSection(rng, paddingModifier)
        VerticalSection(rng, paddingModifier)
    }
}

@Composable
@Suppress("MagicNumber")
private fun SquareItemSection(rng: Random, padding: PaddingValues) {
    SectionHeader(
        name = "Square Items",
        modifier = Modifier,
        padding = padding,
    )

    HorizontalScroller(
        model = HorizontalScrollerListModel(
            dataId = 1_000_000L,
            scrollingItems = List(15) { index ->
                SquareItemListModel(
                    dataId = index.toLong(),
                    name = "Square #$index",
                    sourceInfo = rng.nextInt().toString(),
                    imagePlaceholder = Icon.ALBUM,
                    null,
                    clickAction = VglsAction.Noop,
                )
            }.toImmutableList()
        ),
        PreviewActionSink { },
        modifier = Modifier,
        padding = padding
    )
}

@Composable
@Suppress("MagicNumber")
private fun LoadingSquareItemSection(rng: Random, padding: PaddingValues) {
    val sectionName = "Square Items"
    LoadingSectionHeader(
        seed = sectionName.hashCode().toLong(),
        modifier = Modifier,
        padding = padding,
    )

    HorizontalScroller(
        model = HorizontalScrollerListModel(
            dataId = 1_000_000L,
            scrollingItems = List(15) { index ->
                LoadingItemListModel(
                    loadingType = LoadingType.SQUARE,
                    loadOperationName = sectionName,
                    loadPositionOffset = index,
                )
            }.toImmutableList()
        ),
        PreviewActionSink { },
        modifier = Modifier,
        padding = padding
    )
}

@Composable
@Suppress("MagicNumber")
private fun WideItemSection(rng: Random, padding: PaddingValues) {
    SectionHeader(
        name = "Wide Items",
        modifier = Modifier,
        padding = padding,
    )

    HorizontalScroller(
        model = HorizontalScrollerListModel(
            dataId = 1_000L,
            scrollingItems = List(15) { index ->
                WideItemListModel(
                    dataId = index.toLong(),
                    name = "Wide Item #$index",
                    sourceInfo = rng.nextInt().toString(),
                    Icon.PERSON,
                    null,
                    clickAction = VglsAction.Noop
                )
            }.toImmutableList()
        ),
        PreviewActionSink { },
        modifier = Modifier,
        padding = padding
    )
}

@Composable
@Suppress("MagicNumber")
private fun VerticalSection(rng: Random, padding: PaddingValues) {
    SectionHeader(
        name = "Vertically Scrolling Items",
        modifier = Modifier,
        padding = padding,
    )

    repeat(3) { index ->
        ImageNameListItem(
            model = ImageNameListModel(
                dataId = index.toLong(),
                name = "Wide Item #$index",
                sourceInfo = rng.nextInt().toString(),
                Icon.DESCRIPTION,
                null,
                clickAction = VglsAction.Noop
            ),
            PreviewActionSink { },
            modifier = Modifier,
            padding = padding,
        )
    }
}
