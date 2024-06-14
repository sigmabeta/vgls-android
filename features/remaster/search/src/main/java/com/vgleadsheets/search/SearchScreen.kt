package com.vgleadsheets.search

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.bottombar.SearchState
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.composables.Content
import com.vgleadsheets.composables.SearchBar
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.toImmutableList
import java.util.Random

@Composable
fun SearchScreen(
    state: SearchState,
    actionSink: ActionSink,
    modifier: Modifier = Modifier,
    initialQuery: String = "",
    minSize: Dp = 128.dp
) {
    val items = state.resultItems

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            ),
    ) {
        val searchBarPadding: Dp = 64.dp
        val topInsets = WindowInsets.statusBars

        val contentPadding: PaddingValues = WindowInsets(top = searchBarPadding)
            .add(topInsets)
            .asPaddingValues()

        LazyVerticalGrid(
            contentPadding = contentPadding,
            columns = GridCells.Adaptive(minSize),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        ) {
            items(
                items = items,
                key = { it.dataId },
                contentType = { it.layoutId() },
                span = {
                    if (it.columns < 1) {
                        GridItemSpan(maxLineSpan)
                    } else {
                        GridItemSpan(it.columns)
                    }
                }
            ) {
                it.Content(
                    actionSink = actionSink,
                    modifier = Modifier.padding(horizontal = dimensionResource(com.vgleadsheets.ui.core.R.dimen.margin_side))
                )
            }
        }

        SearchBar(
            actionSink = actionSink,
            modifier = Modifier.padding(top = topInsets.asPaddingValues().calculateTopPadding()),
            initialText = initialQuery,
        )


        Spacer(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(
                    topInsets
                        .asPaddingValues()
                        .calculateTopPadding()
                        .times(2)
                )
                .background(brush = Brush.verticalGradient(colors = scrimColors(isSystemInDarkTheme())))
        )
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
    val state = SearchState(
        resultItems = listData().toImmutableList()
    )

    SearchScreen(
        state = state,
        actionSink = { },
        modifier = Modifier
    )
}


@Suppress("MagicNumber")
private fun listData(): List<ListModel> {
    val rng = Random("HorizontalScroller".hashCode().toLong())

    return squareItemSection(rng) +
        wideItemSection(rng) +
        verticalSection(rng) +
        squareItemSection(rng) +
        verticalSection(rng)

}

@Suppress("MagicNumber")
private fun squareItemSection(rng: Random): List<ListModel> {
    val size = rng.nextInt(15) + 5
    return listOf(
        SectionHeaderListModel(
            title = "Square Items $size",
        ),
        HorizontalScrollerListModel(
            dataId = 1_000_000L + rng.nextInt(1_000),
            scrollingItems = List(size) { index ->
                SquareItemListModel(
                    dataId = index.toLong(),
                    name = "Square #$index",
                    sourceInfo = rng.nextInt().toString(),
                    imagePlaceholder = Icon.ALBUM,
                    null,
                    clickAction = VglsAction.Noop,
                )
            }.toImmutableList()
        )
    )
}

@Suppress("MagicNumber")
private fun wideItemSection(rng: Random): List<ListModel> {
    val size = rng.nextInt(15) + 5
    return listOf(
        SectionHeaderListModel(
            title = "Wide Items $size",
        ),
        HorizontalScrollerListModel(
            dataId = 1_000L + rng.nextInt(1_000),
            scrollingItems = List(size) { index ->
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
    )
}

@Suppress("MagicNumber")
private fun verticalSection(rng: Random): List<ListModel> {
    val size = rng.nextInt(10) + 5
    return listOf(
        SectionHeaderListModel(
            title = "Vertically Scrolling Items $size",
        ),
    ) + List(size) { index ->
        ImageNameListModel(
            dataId = index.toLong() + rng.nextInt(1_000),
            name = "Wide Item #$index",
            sourceInfo = rng.nextInt().toString(),
            Icon.DESCRIPTION,
            null,
            clickAction = VglsAction.Noop
        )
    }
}

@Suppress("MagicNumber")
private fun scrimColors(isDarkTheme: Boolean) = if (isDarkTheme) {
    listOf(
        Color(0, 0, 0, 160),
        Color(0, 0, 0, 64),
        Color(0, 0, 0, 0),
    )
} else {
    listOf(
        Color(255, 255, 255, 255),
        Color(255, 255, 255, 192),
        Color(255, 255, 255, 0),
    )
}
