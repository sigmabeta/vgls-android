package com.vgleadsheets.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vgleadsheets.bottombar.RemasterBottomBar
import com.vgleadsheets.remaster.browse.BrowseScreen
import com.vgleadsheets.remaster.browse.BrowseViewModel
import com.vgleadsheets.remaster.home.HomeScreen
import com.vgleadsheets.remaster.home.HomeViewModel
import com.vgleadsheets.topbar.RemasterTopBar
import com.vgleadsheets.topbar.TopBarViewModel

@Composable
fun RemasterAppUi(
    modifier: Modifier
) {
    val navController = rememberNavController()

    val topBarViewModel: TopBarViewModel = viewModel()

    val titleUpdater = { title: String -> topBarViewModel.updateTitle(title) }
    val subtitleUpdater = { subtitle: String -> topBarViewModel.updateSubtitle(subtitle) }

    Scaffold(
        modifier = modifier,
        topBar = {
            val topBarState by topBarViewModel.uiState.collectAsState()
            RemasterTopBar(topBarState)
        },
        bottomBar = {
            RemasterBottomBar(navController)
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            startDestination = "home"
        ) {
            composable("home") {
                val viewModel: HomeViewModel = hiltViewModel()
                val homeState by viewModel.uiState.collectAsState()

                HomeScreen(homeState)
            }

            composable("browse") {
                val viewModel: BrowseViewModel = hiltViewModel()
                val browseState by viewModel.uiState.collectAsState()

                BrowseScreen(browseState)
            }
        }
    }
}
