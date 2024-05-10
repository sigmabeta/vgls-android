package com.vgleadsheets.remaster.browse

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vgleadsheets.browse.R
import com.vgleadsheets.nav.Destination

fun NavGraphBuilder.browseScreenEntry(
    navigationAction: (String) -> Unit,
    titleUpdater: (String?) -> Unit,
    globalModifier: Modifier
) {
    composable(Destination.BROWSE.noArgs()) {
        val resources = LocalContext.current.resources
        val viewModel = browseViewModel(navigationAction)
        val browseState by viewModel.uiState.collectAsState()

        BrowseScreen(
            title = resources.getString(R.string.title_browse),
            listItems = browseState.items,
            titleUpdater = titleUpdater,
            modifier = globalModifier,
        )
    }
}
