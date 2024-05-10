package com.vgleadsheets.remaster.composers.detail

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vgleadsheets.nav.ARG_DEST_ID
import com.vgleadsheets.nav.Destination

fun NavGraphBuilder.composerDetailScreenEntry(
    navigationAction: (String) -> Unit,
    titleUpdater: (String?) -> Unit,
    globalModifier: Modifier
) {
    composable(
        route = Destination.COMPOSER_DETAIL.idTemplate(),
        arguments = listOf(
            navArgument(ARG_DEST_ID) { type = NavType.LongType }
        )
    ) {
        val composerId = it.arguments?.getLong(ARG_DEST_ID) ?: throw IllegalArgumentException(
            "$ARG_DEST_ID is required"
        )

        val viewModel = composerDetailViewModel(
            composerId = composerId,
            navigateTo = navigationAction,
        )
        val state by viewModel.uiState.collectAsState()

        ComposerDetailScreen(
            title = state.title,
            listItems = state.toListItems(LocalContext.current.resources),
            titleUpdater = titleUpdater,
            modifier = globalModifier,
        )
    }
}
