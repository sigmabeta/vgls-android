package com.vgleadsheets.composables.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import com.vgleadsheets.bottombar.NavBarState
import com.vgleadsheets.bottombar.NavBarVisibility
import com.vgleadsheets.composables.Content
import com.vgleadsheets.scaffold.AppContent
import com.vgleadsheets.scaffold.TopBarConfig
import com.vgleadsheets.strings.VglsStringId
import com.vgleadsheets.topbar.TopBarState
import com.vgleadsheets.topbar.TopBarVisibility
import com.vgleadsheets.ui.theme.AppTheme
import net.sigmabeta.sage.ui.perf.DURATION_THRESHOLD_ERROR_SCREEN_PREVIEW
import net.sigmabeta.sage.ui.perf.DURATION_THRESHOLD_WARNING_SCREEN_PREVIEW
import net.sigmabeta.sage.ui.perf.LocalLogger
import net.sigmabeta.sage.ui.perf.WithMeasurementScreen
import net.sigmabeta.sage.ui.list.GridScreen
import net.sigmabeta.sage.ui.list.ListScreen
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ColumnType
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.list.ListStateActual
import net.sigmabeta.sage.list.WidthClass
import net.sigmabeta.sage.logging.BasicHatchet
import net.sigmabeta.sage.ui.StringProvider
import net.sigmabeta.sage.ui.strings.AndroidStringProvider

@Composable
internal fun ListScreenPreview(
    screenState: ListState,
    darkTheme: Boolean,
    syntheticWidthClass: WidthClass,
    topBarVisibility: TopBarVisibility = TopBarVisibility.VISIBLE,
    navBarVisibility: NavBarVisibility = NavBarVisibility.VISIBLE,
) {
    val actionSink = ActionSink { }
    val stringProvider = AndroidStringProvider(LocalContext.current.resources) { (it as VglsStringId).id() }
    val state = screenState.toActual(stringProvider)

    AppTheme(forceDark = darkTheme) {
        CompositionLocalProvider(
            LocalInspectionMode provides true,
            LocalLogger provides BasicHatchet(),
        ) {
            AppChrome(
                titleBarModel = state.title,
                topBarVisibility = topBarVisibility,
                navBarVisibility = navBarVisibility,
                syntheticWidthClass = syntheticWidthClass,
                content = { innerPadding, widthClass ->
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                    ) {
                        WithMeasurementScreen(
                            "${state.title.title ?: "Unknown"} ($syntheticWidthClass)",
                            DURATION_THRESHOLD_WARNING_SCREEN_PREVIEW,
                            DURATION_THRESHOLD_ERROR_SCREEN_PREVIEW,
                        ) {
                            ListContent(state, widthClass, innerPadding, actionSink)
                        }
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
    val stringProvider = AndroidStringProvider(LocalContext.current.resources) { (it as VglsStringId).id() }

    AppTheme(forceDark = darkTheme) {
        CompositionLocalProvider(
            LocalInspectionMode provides true,
            LocalLogger provides BasicHatchet(),
        ) {
            AppChrome(
                titleBarModel = TitleBarModel(),
                topBarVisibility = topBarVisibility,
                navBarVisibility = navBarVisibility,
                syntheticWidthClass = syntheticWidthClass,
                content = { paddingValues, _ ->
                    Box(
                        modifier = Modifier
                            .padding(paddingValues)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        WithMeasurementScreen(
                            "Non-List Screen ($syntheticWidthClass)",
                            DURATION_THRESHOLD_WARNING_SCREEN_PREVIEW,
                            DURATION_THRESHOLD_ERROR_SCREEN_PREVIEW,
                        ) {
                            content(stringProvider)
                        }
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
    innerPadding: PaddingValues,
    actionSink: ActionSink
) {
    val columnType = state.columnType
    val numColumns = columnType.numberOfColumns(displayWidthClass)

    require(numColumns > 0) {
        "Calculated number of columns is zero for ${state.columnType} and $displayWidthClass."
    }

    val sideMargin = dimensionResource(id = com.vgleadsheets.ui.components.R.dimen.margin_side)
    val itemContent: @Composable (ListModel, ActionSink, Boolean, Modifier, PaddingValues) -> Unit =
        { model, sink, debug, mod, pad -> model.Content(sink, debug, mod, pad) }

    if (numColumns > 1) {
        val (staggered, allowHorizScroller) = if (columnType is ColumnType.Staggered) {
            true to columnType.allowHorizScroller
        } else {
            false to false
        }

        GridScreen(
            state = state,
            actionSink = actionSink,
            showDebug = false,
            numberOfColumns = numColumns,
            staggered = staggered,
            allowHorizScroller = allowHorizScroller,
            sideMargin = sideMargin,
            modifier = Modifier.padding(innerPadding),
            itemContent = itemContent,
        )
    } else {
        ListScreen(
            state = state,
            actionSink = actionSink,
            showDebug = false,
            sideMargin = sideMargin,
            modifier = Modifier.padding(innerPadding),
            itemContent = itemContent,
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
    content: @Composable (PaddingValues, WidthClass) -> Unit,
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
        topBarConfig = topBarConfig,
        navBarState = bottomBarVmState,
        navEventSink = { },
        currentRoute = "",
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        syntheticWidthClass = syntheticWidthClass,
        screen = { innerPadding, widthClass -> content(innerPadding, widthClass) },
    )
}
