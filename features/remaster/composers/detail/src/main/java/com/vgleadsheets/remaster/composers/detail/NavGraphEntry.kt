package com.vgleadsheets.remaster.composers.detail

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

fun NavGraphBuilder.composerDetailScreenEntry(navigationAction: (String) -> Unit, globalModifier: Modifier) {
    composable(
        route = "composers/{composerId}",
        arguments = listOf(
            navArgument("composerId") { type = NavType.LongType }
        )
    ) {
        val composerId = it.arguments?.getLong("composerId") ?: throw IllegalArgumentException(
            "composerId is required"
        )

        val viewModel = composerDetailViewModel(
            composerId = composerId,
            navigationAction,
        )
        val state by viewModel.uiState.collectAsState()

        ComposerDetailScreen(
            state,
            LocalContext.current.resources,
            globalModifier,
        )
    }
}
