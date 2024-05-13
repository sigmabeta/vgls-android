package com.vgleadsheets.remaster.composers.detail

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder

fun NavGraphBuilder.composerDetailScreenEntry(
    navigateTo: (String) -> Unit,
    titleUpdater: (String?) -> Unit,
    globalModifier: Modifier
) {
//    composable(
//        route = Destination.COMPOSER_DETAIL.route,
//        arguments = listOf(
//            navArgument(ARG_LONG) { type = NavType.LongType }
//        )
//    ) {
//        val composerId = it.arguments?.getLong(ARG_LONG) ?: throw IllegalArgumentException(
//            "$ARG_LONG is required"
//        )
//
//        val viewModel = composerDetailViewModel(
//            composerId = composerId,
//            navigateTo = navigateTo,
//        )
//        val state by viewModel.uiState.collectAsState()
//
//        ComposerDetailScreen(
//            title = state.title,
//            listItems = state.toListItems(LocalContext.current.resources),
//            titleUpdater = titleUpdater,
//            modifier = globalModifier,
//        )
//    }
}
