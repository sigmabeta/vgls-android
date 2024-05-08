package com.vgleadsheets.remaster.browse

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vgleadsheets.nav.Destination

fun NavGraphBuilder.browseScreenEntry(navigationAction: (String) -> Unit, globalModifier: Modifier) {
    composable(Destination.BROWSE.noArgs()) {
        val viewModel = browseViewModel(navigationAction)
        val browseState by viewModel.uiState.collectAsState()

        BrowseScreen(browseState, globalModifier)
    }
}
