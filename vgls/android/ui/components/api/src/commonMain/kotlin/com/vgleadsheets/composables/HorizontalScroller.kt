package com.vgleadsheets.composables

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
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.ui.theme.AppTheme
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.HorizontalScrollerListModel
import net.sigmabeta.sage.components.ImageNameListModel
import net.sigmabeta.sage.components.LoadingItemListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.GridImageListModel
import net.sigmabeta.sage.components.WideItemListModel
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.ui.Icon
import java.util.Random

@Composable
@Suppress("MagicNumber")
fun HorizontalScroller(
    model: HorizontalScrollerListModel,
    actionSink: ActionSink,
    showDebug: Boolean,
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
                debug = showDebug,
                mod = Modifier.animateItem(),
                pad = PaddingValues()
            )
        }
    }
}
