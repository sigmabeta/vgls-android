package com.vgleadsheets.remaster.composers.list

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder

fun NavGraphBuilder.composersListScreenEntry(
    navigateTo: (String) -> Unit,
    titleUpdater: (String?) -> Unit,
    globalModifier: Modifier
) {
//    composable(Destination.COMPOSERS_LIST.route) {
//        val resources = LocalContext.current.resources
//        val viewModel = composerListViewModel(
//            navigateTo = navigateTo
//        )
//        val state by viewModel.uiState.collectAsState()
//
//        ComposersListScreen(
//            title = resources.getString(R.string.title_list_composers),
//            listItems = state.toListItems(
//                resources = resources,
//                onComposerClick = { clickedId -> navigateTo(Destination.COMPOSER_DETAIL.forId(clickedId)) }
//            ),
//            titleUpdater = titleUpdater,
//            modifier = globalModifier,
//        )
//    }
}
