package com.vgleadsheets.composables.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.list.ListState
import com.vgleadsheets.list.ListStateActual
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources
import com.vgleadsheets.ui.list.GridScreen
import com.vgleadsheets.ui.list.ListScreen
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
internal fun ListScreenPreviewLight(
    screenState: ListState,
    isGrid: Boolean = false,
) {
    val actionSink = ActionSink { }
    val stringProvider = StringResources(LocalContext.current.resources)
    val state = screenState.toActual(stringProvider)

    VglsMaterial {
        CompositionLocalProvider(LocalInspectionMode provides true) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                ListContent(isGrid, state, actionSink)
            }
        }
    }
}

@Composable
internal fun ListScreenPreviewDark(
    screenState: ListState,
    isGrid: Boolean = false,
) {
    val actionSink = ActionSink { }
    val stringProvider = StringResources(LocalContext.current.resources)
    val state = screenState.toActual(stringProvider)

    VglsMaterial(forceDark = true) {
        CompositionLocalProvider(LocalInspectionMode provides true) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                ListContent(isGrid, state, actionSink)
            }
        }
    }
}

@Composable
internal fun ScreenPreviewLight(
    content: @Composable (StringProvider) -> Unit,
) {
    val stringProvider = StringResources(LocalContext.current.resources)

    VglsMaterial {
        CompositionLocalProvider(LocalInspectionMode provides true) {
            Box(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                content(stringProvider)
            }
        }
    }
}

@Composable
internal fun ScreenPreviewDark(
    content: @Composable (StringProvider) -> Unit,
) {
    val stringProvider = StringResources(LocalContext.current.resources)

    VglsMaterial(forceDark = true) {
        CompositionLocalProvider(LocalInspectionMode provides true) {
            Box(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                content(stringProvider)
            }
        }
    }
}

@Composable
private fun ListContent(
    isGrid: Boolean,
    state: ListStateActual,
    actionSink: ActionSink
) {
    if (isGrid) {
        GridScreen(
            state = state,
            actionSink = actionSink,
            showDebug = false,
            modifier = Modifier,
        )
    } else {
        ListScreen(
            state = state,
            actionSink = actionSink,
            showDebug = false,
            modifier = Modifier,
        )
    }
}
