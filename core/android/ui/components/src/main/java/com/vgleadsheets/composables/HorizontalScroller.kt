package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.toImmutableList
import java.util.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalScroller(
    model: HorizontalScrollerListModel,
    modifier: Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.margin_side)),
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
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .animateItemPlacement()
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

        SquareItemSection(rng)
        WideItemSection(rng)
        VerticalSection(rng)
        SquareItemSection(rng)
        VerticalSection(rng)
    }
}

@Composable
private fun SquareItemSection(rng: Random) {
    SectionHeader(
        name = "Square Items",
        modifier = Modifier
    )

    HorizontalScroller(
        model = HorizontalScrollerListModel(
            dataId = 1_000_000L,
            scrollingItems = List(15) { index ->
                SquareItemListModel(
                    dataId = index.toLong(),
                    name = "Square #$index",
                    imageUrl = rng.nextInt().toString(),
                    imagePlaceholder = Icon.ALBUM,
                    null,
                ) { }
            }.toImmutableList()
        ),
        modifier = Modifier
    )
}

@Composable
private fun WideItemSection(rng: Random) {
    SectionHeader(
        name = "Wide Items",
        modifier = Modifier
    )

    HorizontalScroller(
        model = HorizontalScrollerListModel(
            dataId = 1_000L,
            scrollingItems = List(15) { index ->
                WideItemListModel(
                    dataId = index.toLong(),
                    name = "Wide Item #$index",
                    imageUrl = rng.nextInt().toString(),
                    Icon.PERSON,
                    null,
                ) { }
            }.toImmutableList()
        ),
        modifier = Modifier
    )
}

@Composable
private fun VerticalSection(rng: Random) {
    SectionHeader(
        name = "Vertically Scrolling Items",
        modifier = Modifier
    )

    repeat(3) { index ->
        ImageNameListItem(
            model = ImageNameListModel(
                dataId = index.toLong(),
                name = "Wide Item #$index",
                imageUrl = rng.nextInt().toString(),
                Icon.DESCRIPTION,
                null,
            ) { },
            modifier = Modifier,
        )
    }
}
