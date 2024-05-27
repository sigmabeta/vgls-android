package com.vgleadsheets.ui.list

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.nav.ARG_TEMPLATE
import com.vgleadsheets.nav.ArgType
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.viewmodel.list.listViewModel

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.listScreenEntry(
    destination: Destination,
    eventDispatcher: EventDispatcher,
    topBarState: TopAppBarState,
    globalModifier: Modifier,
) {
    composable(
        route = destination.template(),
        arguments = if (destination.argType == ArgType.NONE) {
            emptyList()
        } else {
            listOf(navArgument(ARG_TEMPLATE) { type = destination.argType.toNavType() })
        },
    ) { navBackStackEntry ->
        val idArg = if (destination.argType == ArgType.LONG) {
            navBackStackEntry.arguments?.getLong(ARG_TEMPLATE)
        } else {
            null
        }

        val stringArg = if (destination.argType == ArgType.STRING) {
            navBackStackEntry.arguments?.getString(ARG_TEMPLATE)
        } else {
            null
        }

        val viewModel = listViewModel(
            destination = destination,
            eventDispatcher = eventDispatcher,
            idArg = idArg ?: 0L,
            stringArg = stringArg,
        )

        LaunchedEffect(Unit) {
            topBarState.heightOffset = 0.0f
            topBarState.contentOffset = 0.0f
            viewModel.sendAction(VglsAction.Resume)
        }

        BackHandler(viewModel.handlesBack) { viewModel.sendAction(VglsAction.DeviceBack) }

        if (destination.renderAsGrid) {
            GridScreen(
                stateSource = viewModel.uiState,
                actionSink = viewModel,
                modifier = globalModifier,
            )
        } else {
            ListScreen(
                stateSource = viewModel.uiState,
                actionSink = viewModel,
                modifier = globalModifier,
            )
        }
    }
}