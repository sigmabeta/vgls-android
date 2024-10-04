package com.vgleadsheets.ui.licenses

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.nav.Destination

fun NavGraphBuilder.licensesScreenNavEntry(
    globalModifier: Modifier,
) {
    composable(
        route = Destination.LICENSES.template(),
    ) {
        val viewModel: LicenseViewModel = hiltViewModel()

        DisposableEffect(Unit) {
            viewModel.sendAction(VglsAction.Resume)

            onDispose {
                viewModel.sendAction(VglsAction.Pause)
            }
        }

        BackHandler(true) { viewModel.sendAction(VglsAction.DeviceBack) }

        val state by viewModel.uiState.collectAsStateWithLifecycle()

        LicenseScreen(
            state = state,
            modifier = globalModifier
        )
    }
}
