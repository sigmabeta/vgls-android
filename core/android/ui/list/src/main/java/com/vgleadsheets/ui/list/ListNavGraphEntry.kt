package com.vgleadsheets.ui.list

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.list.ColumnType
import com.vgleadsheets.list.WidthClass
import com.vgleadsheets.nav.ARG_TEMPLATE_ONE
import com.vgleadsheets.nav.ArgType
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.perf.DURATION_THRESHOLD_ERROR_SCREEN_DEVICE
import com.vgleadsheets.perf.DURATION_THRESHOLD_WARNING_SCREEN_DEVICE
import com.vgleadsheets.perf.WithMeasurementScreen
import com.vgleadsheets.viewmodel.list.listViewModel

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
                viewModel.sendAction(VglsAction.Resume)

                onDispose {
                    viewModel.sendAction(VglsAction.Pause)
                }
            }

            BackHandler(true) { viewModel.sendAction(VglsAction.DeviceBack) }
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val showDebug by viewModel.showDebug.collectAsStateWithLifecycle()

            val columnType = state.columnType
            val numColumns = columnType.numberOfColumns(displayWidthClass)

            require(numColumns > 0) {
                "Calculated number of columns is zero for $columnType and $displayWidthClass."
            }

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
                    modifier = globalModifier,
                )
            } else {
                ListScreen(
                    state = state,
                    actionSink = viewModel,
                    showDebug = showDebug,
                    modifier = globalModifier,
                )
            }
        }
    }
}
