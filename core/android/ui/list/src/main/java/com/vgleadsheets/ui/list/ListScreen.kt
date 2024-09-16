package com.vgleadsheets.ui.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.composables.Content
import com.vgleadsheets.list.ListStateActual

@Composable
fun ListScreen(
    state: ListStateActual,
    actionSink: ActionSink,
    showDebug: Boolean,
    modifier: Modifier
) {
    val title = state.title
    val items = state.listItems

    if (title.title != null) {
        LaunchedEffect(title.title) {
            actionSink.sendAction(VglsAction.Resume)
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = items,
            key = { it.dataId },
            contentType = { it.layoutId() }
        ) {
            it.Content(
                sink = actionSink,
                mod = Modifier.animateItem(),
                debug = showDebug,
                pad = PaddingValues(horizontal = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.margin_side))
            )
        }
    }
}
