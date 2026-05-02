package com.vgleadsheets.ui.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vgleadsheets.composables.Content
import com.vgleadsheets.viewmodel.list.listViewModel
import net.sigmabeta.sage.android.perf.DURATION_THRESHOLD_ERROR_SCREEN_DEVICE
import net.sigmabeta.sage.android.perf.DURATION_THRESHOLD_WARNING_SCREEN_DEVICE
import net.sigmabeta.sage.android.perf.WithMeasurementScreen
import net.sigmabeta.sage.android.ui.list.GridScreen
import net.sigmabeta.sage.android.ui.list.ListScreen
import net.sigmabeta.sage.android.ui.list.toNavType
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.list.ColumnType
import net.sigmabeta.sage.list.WidthClass
import net.sigmabeta.sage.nav.ARG_TEMPLATE_ONE
import net.sigmabeta.sage.nav.ArgType
import net.sigmabeta.sage.nav.Destination

@Suppress("LongMethod")
fun NavGraphBuilder.listScreenEntry(
    destination: Destination,
    displayWidthClass: WidthClass,
    globalModifier: Modifier,
) {
    composable(
        route = destination.template(),
        arguments = if (destination.argType == ArgType.NONE) {
            emptyList()
        } else {
            listOf(navArgument(ARG_TEMPLATE_ONE) { type = destination.argType.toNavType() })
        },
    ) { navBackStackEntry ->

        WithMeasurementScreen(
            navBackStackEntry.destination.route ?: "Unknown",
            DURATION_THRESHOLD_WARNING_SCREEN_DEVICE,
            DURATION_THRESHOLD_ERROR_SCREEN_DEVICE,
        ) {
            val idArg = if (destination.argType == ArgType.LONG) {
                navBackStackEntry.arguments?.getLong(ARG_TEMPLATE_ONE)
            } else {
                null
            }

            val stringArg = if (destination.argType == ArgType.STRING) {
                navBackStackEntry.arguments?.getString(ARG_TEMPLATE_ONE)
            } else {
                null
            }

            val viewModel = listViewModel(
                destination = destination,
                idArg = idArg ?: 0L,
                stringArg = stringArg,
            )

            DisposableEffect(Unit) {
                viewModel.sendAction(SageAction.Resume)

                onDispose {
                    viewModel.sendAction(SageAction.Pause)
                }
            }

            BackHandler(true) { viewModel.sendAction(SageAction.DeviceBack) }
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val showDebug by viewModel.showDebug.collectAsStateWithLifecycle()

            val columnType = state.columnType
            val numColumns = columnType.numberOfColumns(displayWidthClass)

            require(numColumns > 0) {
                "Calculated number of columns is zero for $columnType and $displayWidthClass."
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
                    actionSink = viewModel,
                    showDebug = showDebug,
                    numberOfColumns = numColumns,
                    staggered = staggered,
                    allowHorizScroller = allowHorizScroller,
                    sideMargin = sideMargin,
                    modifier = globalModifier,
                    itemContent = itemContent,
                )
            } else {
                ListScreen(
                    state = state,
                    actionSink = viewModel,
                    showDebug = showDebug,
                    sideMargin = sideMargin,
                    modifier = globalModifier,
                    itemContent = itemContent,
                )
            }
        }
    }
}
