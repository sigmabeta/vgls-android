package com.vgleadsheets.scaffold

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vgleadsheets.bottombar.RemasterBottomBar
import com.vgleadsheets.remaster.browse.browseScreenEntry
import com.vgleadsheets.remaster.composers.detail.composerDetailScreenEntry
import com.vgleadsheets.remaster.composers.list.composersListScreenEntry
import com.vgleadsheets.remaster.games.detail.gameDetailScreenEntry
import com.vgleadsheets.remaster.games.list.gamesListScreenEntry
import com.vgleadsheets.remaster.home.homeScreenEntry
import com.vgleadsheets.topbar.RemasterTopBar
import com.vgleadsheets.topbar.TopBarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemasterAppUi(
    modifier: Modifier
) {
    val navController = rememberNavController()

    val topBarViewModel: TopBarViewModel = viewModel()

    val titleUpdater = { title: String? ->
        topBarViewModel.updateTitle(title)
    }
    val subtitleUpdater = { subtitle: String? -> topBarViewModel.updateSubtitle(subtitle) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val topBarState by topBarViewModel.uiState.collectAsState()
            RemasterTopBar(topBarState, scrollBehavior)
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
            val globalModifier = Modifier.fillMaxSize()
            val navigationAction = { linkUri: String ->
                navController.navigate(linkUri)
            }

            homeScreenEntry(navigationAction, titleUpdater, globalModifier)
            browseScreenEntry(navigationAction, titleUpdater, globalModifier)
            gamesListScreenEntry(navigationAction, titleUpdater, globalModifier)
            gameDetailScreenEntry(navigationAction, titleUpdater, globalModifier)
            composersListScreenEntry(navigationAction, titleUpdater, globalModifier)
            composerDetailScreenEntry(navigationAction, titleUpdater, globalModifier)
        }
    }
}
