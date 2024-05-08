package com.vgleadsheets.remaster.composers.list

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vgleadsheets.nav.Destination

fun NavGraphBuilder.composersListScreenEntry(navigationAction: (String) -> Unit, globalModifier: Modifier) {
    composable(Destination.COMPOSERS_LIST.noArgs()) {
        val viewModel = composerListViewModel(navigationAction)
        val state by viewModel.uiState.collectAsState()

        ComposersListScreen(state, globalModifier)
    }
}
