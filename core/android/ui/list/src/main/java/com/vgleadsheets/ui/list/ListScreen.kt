package com.vgleadsheets.ui.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.composables.Content
import com.vgleadsheets.list.ListStateActual
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ListScreen(
    stateSource: StateFlow<ListStateActual>,
    actionSink: ActionSink,
    modifier: Modifier
) {
    val state by stateSource.collectAsStateWithLifecycle()

    val title = state.title
    val items = state.listItems

    if (title.title != null) {
        LaunchedEffect(Unit) {
            actionSink.sendAction(VglsAction.Resume)
        }
    }

    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = items,
            key = { it.dataId },
            contentType = { it.layoutId() }
        ) {
            it.Content(
                actionSink = actionSink,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.margin_side)
                )
            )
        }
    }
}
