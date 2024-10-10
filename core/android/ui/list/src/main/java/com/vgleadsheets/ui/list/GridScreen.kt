package com.vgleadsheets.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.SectionListModel
import com.vgleadsheets.composables.Content
import com.vgleadsheets.list.ListStateActual
import kotlinx.collections.immutable.toImmutableList

@Composable
@Suppress("MagicNumber", "LongMethod")
fun GridScreen(
    state: ListStateActual,
    actionSink: ActionSink,
    showDebug: Boolean,
    numberOfColumns: Int,
    staggered: Boolean,
    allowHorizScroller: Boolean,
    modifier: Modifier,
) {
    val title = state.title
    val items = state.listItems

    if (title.title != null) {
        LaunchedEffect(title.title) {
            actionSink.sendAction(VglsAction.Resume)
        }
    }

    val unrolledItems = items.map { topLevelItem ->
        return@map if (topLevelItem is SectionListModel) {
            if (topLevelItem.dontUnroll) {
                return@map topLevelItem
            }

            val horizontalScrollers = topLevelItem.sectionItems.filterIsInstance<HorizontalScrollerListModel>()

            if (horizontalScrollers.isNotEmpty()) {
                val otherItems = topLevelItem.sectionItems.filter { it !is HorizontalScrollerListModel }
                val flattenedItems = otherItems + horizontalScrollers.map { it.scrollingItems }.flatten()

                topLevelItem.copy(sectionItems = flattenedItems.toImmutableList())
            } else {
                topLevelItem
            }
        } else {
            topLevelItem
        }
    }

    if (staggered) {
        val (contentPadding, itemPadding, arrangement) = if (allowHorizScroller) {
            Triple(
                0.dp,
                dimensionResource(id = com.vgleadsheets.ui.components.R.dimen.margin_side) / 2,
                Arrangement.spacedBy(0.dp)
            )
        } else {
            Triple(
                dimensionResource(id = com.vgleadsheets.ui.components.R.dimen.margin_side),
                0.dp,
                Arrangement.spacedBy(32.dp)
            )
        }

        val contentPaddingWithInsets = PaddingValues(
            top = 16.dp,
            start = contentPadding,
            end = contentPadding,
            bottom = 16.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        )

        LazyVerticalStaggeredGrid(
            contentPadding = contentPaddingWithInsets,
            columns = StaggeredGridCells.Fixed(numberOfColumns),
            horizontalArrangement = arrangement,
            modifier = modifier
                .fillMaxSize()
        ) {
            items(
                items = unrolledItems,
                key = { it.dataId },
                contentType = { it.layoutId() },
                span = {
                    if (it.columns < 1) {
                        StaggeredGridItemSpan.FullLine
                    } else {
                        StaggeredGridItemSpan.SingleLane
                    }
                }
            ) {
                it.Content(
                    sink = actionSink,
                    mod = Modifier.animateItem(),
                    debug = showDebug,
                    pad = PaddingValues(horizontal = itemPadding)
                )
            }
        }
    } else {
        val contentPaddingWithInsets = PaddingValues(
            top = 16.dp,
            bottom = 16.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            start = dimensionResource(id = com.vgleadsheets.ui.components.R.dimen.margin_side),
            end = dimensionResource(id = com.vgleadsheets.ui.components.R.dimen.margin_side),
        )

        LazyVerticalGrid(
            contentPadding = contentPaddingWithInsets,
            columns = GridCells.Fixed(numberOfColumns),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxSize()
        ) {
            items(
                items = unrolledItems,
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
                    sink = actionSink,
                    mod = Modifier.animateItem(),
                    debug = showDebug,
                    pad = PaddingValues()
                )
            }
        }
    }
}
