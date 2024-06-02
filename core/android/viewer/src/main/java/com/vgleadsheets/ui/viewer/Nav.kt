package com.vgleadsheets.ui.viewer

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.nav.ARG_TEMPLATE_ONE
import com.vgleadsheets.nav.ARG_TEMPLATE_TWO
import com.vgleadsheets.nav.Destination

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.viewerScreenNavEntry(
    eventDispatcher: EventDispatcher,
    topBarState: TopAppBarState,
    globalModifier: Modifier,
) {
    composable(
        route = Destination.SONG_VIEWER.template(),
        arguments = listOf(
            navArgument(ARG_TEMPLATE_ONE) { type = NavType.LongType },
            navArgument(ARG_TEMPLATE_TWO) { type = NavType.LongType },
        ),
    ) { navBackStackEntry ->
        val idArg = navBackStackEntry.arguments?.getLong(ARG_TEMPLATE_ONE)
        val pageArg = navBackStackEntry.arguments?.getLong(ARG_TEMPLATE_TWO)

        val viewModel = viewerViewModel(
            eventDispatcher = eventDispatcher,
            idArg = idArg ?: 0L,
            pageArg = pageArg ?: 0L,
        )

        LaunchedEffect(Unit) {
            topBarState.heightOffset = 0.0f
            topBarState.contentOffset = 0.0f
            viewModel.sendAction(VglsAction.Resume)
        }

        BackHandler(true) { viewModel.sendAction(VglsAction.DeviceBack) }

        ViewerScreen(
            viewModel.uiState,
            actionSink = viewModel,
            modifier = globalModifier
        )
    }
}
