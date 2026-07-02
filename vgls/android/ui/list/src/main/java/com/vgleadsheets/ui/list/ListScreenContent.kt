package com.vgleadsheets.ui.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vgleadsheets.composables.Content
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.list.ColumnType
import net.sigmabeta.sage.list.WidthClass
import net.sigmabeta.sage.ui.list.GridScreen
import net.sigmabeta.sage.ui.list.ListScreen
import net.sigmabeta.sage.ui.perf.DURATION_THRESHOLD_ERROR_SCREEN_DEVICE
import net.sigmabeta.sage.ui.perf.DURATION_THRESHOLD_WARNING_SCREEN_DEVICE
import net.sigmabeta.sage.ui.perf.WithMeasurementScreen

/**
 * Renders a [VglsListViewModel]'s [VglsListViewModel.uiStateActual] as a [ListScreen] or [GridScreen]
 * (by column count), wiring Resume/Pause/DeviceBack. Shared by every per-feature nav entry — the
 * replacement for the old generic `listScreenEntry` that resolved a single `ListViewModel` shell per
 * destination via `BrainProvider`.
 */
@Composable
fun ListScreenContent(
    viewModel: VglsListViewModel<*>,
    screenName: String,
    displayWidthClass: WidthClass,
    globalModifier: Modifier,
) {
    WithMeasurementScreen(
        screenName,
        DURATION_THRESHOLD_WARNING_SCREEN_DEVICE,
        DURATION_THRESHOLD_ERROR_SCREEN_DEVICE,
    ) {
        DisposableEffect(Unit) {
            viewModel.sendAction(SageAction.Resume)
            onDispose { viewModel.sendAction(SageAction.Pause) }
        }

        BackHandler(true) { viewModel.sendAction(SageAction.DeviceBack) }

        val state by viewModel.uiStateActual.collectAsStateWithLifecycle()
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
