package com.vgleadsheets.ui.viewer

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.nav.ARG_TEMPLATE_ONE
import com.vgleadsheets.nav.ARG_TEMPLATE_TWO
import com.vgleadsheets.nav.Destination

fun NavGraphBuilder.viewerScreenNavEntry(
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
            idArg = idArg ?: 0L,
            pageArg = pageArg ?: 0L,
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

        ViewerScreen(
            state = state,
            actionSink = viewModel,
            showDebug = showDebug,
            modifier = globalModifier
        )
    }
}
