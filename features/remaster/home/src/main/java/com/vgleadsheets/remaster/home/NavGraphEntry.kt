package com.vgleadsheets.remaster.home

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vgleadsheets.nav.Destination

fun NavGraphBuilder.homeScreenEntry(navigationAction: (String) -> Unit, globalModifier: Modifier) {
    composable(Destination.HOME.noArgs()) {
        val viewModel = homeViewModel(navigationAction)
        val state by viewModel.uiState.collectAsState()

        HomeScreen(state, navigationAction, globalModifier)
    }
}
