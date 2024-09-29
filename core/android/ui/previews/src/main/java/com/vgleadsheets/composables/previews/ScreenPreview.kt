package com.vgleadsheets.composables.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.bottombar.NavBarState
import com.vgleadsheets.bottombar.NavBarVisibility
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.list.ListStateActual
import com.vgleadsheets.list.WidthClass
import com.vgleadsheets.scaffold.AppContent
import com.vgleadsheets.scaffold.TopBarConfig
import com.vgleadsheets.topbar.TopBarState
import com.vgleadsheets.topbar.TopBarVisibility
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources
import com.vgleadsheets.ui.list.GridScreen
import com.vgleadsheets.ui.list.ListScreen
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
internal fun ListScreenPreview(
    screenState: ListState,
    darkTheme: Boolean,
    syntheticWidthClass: WidthClass,
    topBarVisibility: TopBarVisibility = TopBarVisibility.VISIBLE,
    navBarVisibility: NavBarVisibility = NavBarVisibility.VISIBLE,
) {
    val actionSink = ActionSink { }
    val stringProvider = StringResources(LocalContext.current.resources)
    val state = screenState.toActual(stringProvider)

    VglsMaterial(forceDark = darkTheme) {
        CompositionLocalProvider(LocalInspectionMode provides true) {
            AppChrome(
                titleBarModel = state.title,
                topBarVisibility = topBarVisibility,
                navBarVisibility = navBarVisibility,
                syntheticWidthClass = syntheticWidthClass,
                content = { widthClass ->
                    Box(
                        modifier = Modifier
                            .padding(top = 72.dp)
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                    ) {
                        ListContent(state, widthClass, actionSink)
                    }
                },
            )
        }
    }
}

@Composable
internal fun ScreenPreview(
    darkTheme: Boolean,
    topBarVisibility: TopBarVisibility = TopBarVisibility.VISIBLE,
    navBarVisibility: NavBarVisibility = NavBarVisibility.VISIBLE,
    syntheticWidthClass: WidthClass,
    content: @Composable (StringProvider) -> Unit,
) {
    val stringProvider = StringResources(LocalContext.current.resources)

    VglsMaterial(forceDark = darkTheme) {
        CompositionLocalProvider(LocalInspectionMode provides true) {
            AppChrome(
                titleBarModel = TitleBarModel(),
                topBarVisibility = topBarVisibility,
                navBarVisibility = navBarVisibility,
                syntheticWidthClass = syntheticWidthClass,
                content = {
                    Box(
                        modifier = Modifier
                            .padding(top = 72.dp)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        content(stringProvider)
                    }
                },
            )
        }
    }
}

/**
 * Roughly equivalent to NavGraphBuilder.listScreenEntry()
 */
@Composable
private fun ListContent(
    state: ListStateActual,
    displayWidthClass: WidthClass,
    actionSink: ActionSink
) {
    val numColumns = state.columnType.numberOfColumns(displayWidthClass)

    require(numColumns > 0) {
        "Calculated number of columns is zero for ${state.columnType} and $displayWidthClass."
    }

    if (numColumns > 1) {
        GridScreen(
            state = state,
            actionSink = actionSink,
            showDebug = false,
            numberOfColumns = numColumns,
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

/**
 * Roughly equivalent to RemasterAppUi
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppChrome(
    titleBarModel: TitleBarModel,
    topBarVisibility: TopBarVisibility,
    navBarVisibility: NavBarVisibility,
    syntheticWidthClass: WidthClass,
    content: @Composable (WidthClass) -> Unit,
) {
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)
    val topBarVmState = TopBarState(
        model = titleBarModel,
        visibility = topBarVisibility,
    )
    val topBarConfig = TopBarConfig(
        state = topBarVmState,
        behavior = scrollBehavior,
        handleAction = { },
    )
    val bottomBarVmState = NavBarState(
        visibility = navBarVisibility
    )

    topBarState.heightOffset = 0.0f
    topBarState.contentOffset = 0.0f

    AppContent(
        screen = { _, widthClass -> content(widthClass) },
        topBarConfig = topBarConfig,
        navBarState = bottomBarVmState,
        syntheticWidthClass = syntheticWidthClass,
        modifier = Modifier,
    )
}
