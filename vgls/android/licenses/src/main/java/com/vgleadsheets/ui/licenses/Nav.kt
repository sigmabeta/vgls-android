package com.vgleadsheets.ui.licenses

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.zacsweers.metrox.viewmodel.metroViewModel
import net.sigmabeta.sage.appcomm.SageAction
import com.vgleadsheets.nav.Destination

fun NavGraphBuilder.licensesScreenNavEntry(
    globalModifier: Modifier,
) {
    composable(
        route = Destination.LICENSES.template(),
    ) {
        val viewModel: LicenseViewModel = metroViewModel()

        DisposableEffect(Unit) {
            viewModel.sendAction(SageAction.Resume)

            onDispose {
                viewModel.sendAction(SageAction.Pause)
            }
        }

        BackHandler(true) { viewModel.sendAction(SageAction.DeviceBack) }

        val state by viewModel.uiState.collectAsStateWithLifecycle()

        LicenseScreen(
            state = state,
            modifier = globalModifier
        )
    }
}
