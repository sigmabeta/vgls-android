package com.vgleadsheets.composables.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.bottombar.BottomBarState
import com.vgleadsheets.bottombar.BottomBarVisibility
import com.vgleadsheets.bottombar.RemasterBottomBar
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.list.ListStateActual
import com.vgleadsheets.scaffold.AppContent
import com.vgleadsheets.topbar.RemasterTopBar
import com.vgleadsheets.topbar.TopBarState
import com.vgleadsheets.topbar.TopBarVisibility
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
    ListScreenPreview(screenState, isGrid, false)
}

@Composable
internal fun ListScreenPreviewDark(
    screenState: ListState,
    isGrid: Boolean = false,
) {
    ListScreenPreview(screenState, isGrid, true)
}

@Composable
private fun ListScreenPreview(screenState: ListState, isGrid: Boolean, inDarkMode: Boolean) {
    val actionSink = ActionSink { }
    val stringProvider = StringResources(LocalContext.current.resources)
    val state = screenState.toActual(stringProvider)

    VglsMaterial(forceDark = inDarkMode) {
        CompositionLocalProvider(LocalInspectionMode provides true) {
            AppChrome(state.title) {
                Box(
                    modifier = Modifier
                        .padding(top = 72.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                ) {
                    ListContent(isGrid, state, actionSink)
                }
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
            AppChrome(TitleBarModel()) {
                Box(
                    modifier = Modifier
                        .padding(top = 72.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    content(stringProvider)
                }
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
            AppChrome(TitleBarModel()) {
                Box(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                ) {
                    content(stringProvider)
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppChrome(
    titleBarModel: TitleBarModel,
    content: @Composable (StringProvider) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val topBarVmState = TopBarState(
        titleBarModel,
        visibility = TopBarVisibility.VISIBLE
    )
    val bottomBarVmState = BottomBarState(
        visibility = BottomBarVisibility.VISIBLE
    )

    val stringProvider = StringResources(LocalContext.current.resources)
    AppContent(
        mainContent = { content(stringProvider) },
        topBarContent = { RemasterTopBar(topBarVmState, scrollBehavior) { } },
        bottomBarContent = { RemasterBottomBar(bottomBarVmState, rememberNavController()) },
        snackbarHost = { },
        modifier = Modifier,
    )
}
