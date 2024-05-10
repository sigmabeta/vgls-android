package com.vgleadsheets.remaster.composers.list

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vgleadsheets.composers.list.R
import com.vgleadsheets.nav.Destination

fun NavGraphBuilder.composersListScreenEntry(
    navigationAction: (String) -> Unit,
    titleUpdater: (String?) -> Unit,
    globalModifier: Modifier
) {
    composable(Destination.COMPOSERS_LIST.noArgs()) {
        val resources = LocalContext.current.resources
        val viewModel = composerListViewModel(
            navigateTo = navigationAction
        )
        val state by viewModel.uiState.collectAsState()

        ComposersListScreen(
            title = resources.getString(R.string.title_list_composers),
            listItems = state.toListItems(
                resources = resources,
                onComposerClick = { clickedId -> navigationAction(Destination.COMPOSER_DETAIL.forId(clickedId)) }
            ),
            titleUpdater = titleUpdater,
            modifier = globalModifier,
        )
    }
}
