package com.vgleadsheets.ui.list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListEvent
import com.vgleadsheets.nav.ARG_TEMPLATE
import com.vgleadsheets.nav.ArgType
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.viewmodel.list.listViewModel

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.listScreenEntry(
    destination: Destination,
    navigateTo: (String) -> Unit,
    titleUpdater: (TitleBarModel) -> Unit,
    topBarState: TopAppBarState,
    globalModifier: Modifier
) {
    composable(
        route = destination.template(),
        arguments = if (destination.argType == ArgType.NONE) {
            emptyList()
        } else {
            listOf(navArgument(ARG_TEMPLATE) { type = destination.argType.toNavType() })
        }
    ) {
        val viewModel = listViewModel(
            destination = destination,
            eventHandler = eventHandler(navigateTo),
            idArg = it.arguments?.getLong(ARG_TEMPLATE) ?: 0L,
            stringArg = it.arguments?.getString(ARG_TEMPLATE)
        )

        LaunchedEffect(Unit) {
            topBarState.heightOffset = 0.0f
            topBarState.contentOffset = 0.0f
            viewModel.handleAction(VglsAction.Resume)
        }

        if (destination.renderAsGrid) {
            GridScreen(
                stateSource = viewModel.uiState,
                actionHandler = viewModel.actionHandler,
                titleUpdater = titleUpdater,
                modifier = globalModifier,
            )
        } else {
            ListScreen(
                stateSource = viewModel.uiState,
                actionHandler = viewModel.actionHandler,
                titleUpdater = titleUpdater,
                modifier = globalModifier,
            )
        }
    }
}

private fun eventHandler(
    navigateTo: (String) -> Unit
): (ListEvent) -> Unit {
    return { event ->
        println("Handling event: $event")
        when (event) {
            is ListEvent.NavigateTo -> navigateTo(event.destination)
        }
    }
}
