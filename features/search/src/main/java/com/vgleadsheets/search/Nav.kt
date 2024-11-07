package com.vgleadsheets.search

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.nav.Destination

fun NavGraphBuilder.searchScreenNavEntry(
    globalModifier: Modifier,
) {
    composable(
        route = Destination.SEARCH.template(),
    ) {
        var searchText by rememberSaveable { mutableStateOf("") }
        val textFieldUpdater = { newText: String -> searchText = newText }

        val viewModel: SearchViewModel = searchViewModel(textFieldUpdater)

        DisposableEffect(Unit) {
            viewModel.sendAction(VglsAction.Resume)

            onDispose {
                viewModel.sendAction(VglsAction.Pause)
            }
        }

        BackHandler(true) { viewModel.sendAction(VglsAction.DeviceBack) }

        val state by viewModel.uiState.collectAsStateWithLifecycle()
        val showDebug by viewModel.showDebug.collectAsStateWithLifecycle()

        SearchScreen(
            query = searchText,
            results = state.toListItems(viewModel.stringProvider),
            textFieldUpdater = textFieldUpdater,
            showDebug = showDebug,
            actionSink = viewModel,
            modifier = globalModifier
        )
    }
}
