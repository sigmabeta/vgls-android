package com.vgleadsheets.search

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.composables.Content
import com.vgleadsheets.composables.SearchBar
import kotlinx.collections.immutable.ImmutableList

@Composable
@Suppress("LongMethod")
fun SearchScreen(
    query: String,
    results: ImmutableList<ListModel>,
    actionSink: ActionSink,
    textFieldUpdater: (String) -> Unit,
    showDebug: Boolean,
    modifier: Modifier = Modifier,
    minSize: Dp = 128.dp
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            ),
    ) {
        val searchBarPadding: Dp = 72.dp
        val topInsets = WindowInsets.statusBars
        val sidePadding = WindowInsets(
            left = dimensionResource(com.vgleadsheets.ui.core.R.dimen.margin_side),
            right = dimensionResource(com.vgleadsheets.ui.core.R.dimen.margin_side),
        )

        val contentPadding: PaddingValues = WindowInsets(top = searchBarPadding)
            .add(topInsets)
            .add(sidePadding)
            .asPaddingValues()

        LazyVerticalGrid(
            contentPadding = contentPadding,
            columns = GridCells.Adaptive(minSize),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = results,
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
                    debug = showDebug,
                    mod = Modifier.animateItem(),
                    pad = PaddingValues(),
                )
            }
        }

        SearchBar(
            text = query,
            textFieldUpdater = textFieldUpdater,
            actionSink = actionSink,
            modifier = Modifier.padding(top = topInsets.asPaddingValues().calculateTopPadding()),
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
