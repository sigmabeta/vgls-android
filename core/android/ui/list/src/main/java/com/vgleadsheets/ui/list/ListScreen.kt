package com.vgleadsheets.ui.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.composables.Content
import com.vgleadsheets.list.ListStateActual
import com.vgleadsheets.state.VglsAction
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ListScreen(
    stateSource: StateFlow<ListStateActual>,
    titleUpdater: (TitleBarModel) -> Unit,
    actionHandler: (VglsAction) -> Unit,
    modifier: Modifier
) {
    val state by stateSource.collectAsStateWithLifecycle()

    val title = state.title
    val items = state.listItems

    LaunchedEffect(Unit) {
        titleUpdater(title)
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
                actionHandler = actionHandler,
                modifier = Modifier
            )
        }
    }
}
