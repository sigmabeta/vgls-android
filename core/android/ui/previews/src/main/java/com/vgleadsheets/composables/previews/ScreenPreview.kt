package com.vgleadsheets.composables.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.list.ListState
import com.vgleadsheets.list.ListStateActual
import com.vgleadsheets.ui.StringResources
import com.vgleadsheets.ui.list.GridScreen
import com.vgleadsheets.ui.list.ListScreen
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
internal fun ScreenPreviewLight(
    screenState: ListState,
    isGrid: Boolean = false,
) {
    val actionSink = ActionSink { }
    val stringProvider = StringResources(LocalContext.current.resources)
    val state = screenState.toActual(stringProvider)

    VglsMaterial {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Content(isGrid, state, actionSink)
        }
    }
}

@Composable
internal fun ScreenPreviewDark(
    screenState: ListState,
    isGrid: Boolean = false,
) {
    val actionSink = ActionSink { }
    val stringProvider = StringResources(LocalContext.current.resources)
    val state = screenState.toActual(stringProvider)

    VglsMaterial(forceDark = true) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Content(isGrid, state, actionSink)
        }
    }
}

@Composable
private fun Content(
    isGrid: Boolean,
    state: ListStateActual,
    actionSink: ActionSink
) {
    if (isGrid) {
        GridScreen(
            state = state,
            actionSink = actionSink,
            modifier = Modifier,
        )
    } else {
        ListScreen(
            state = state,
            actionSink = actionSink,
            modifier = Modifier,
        )
    }
}
