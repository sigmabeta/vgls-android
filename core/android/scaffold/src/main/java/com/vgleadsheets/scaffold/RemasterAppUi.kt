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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vgleadsheets.bottombar.RemasterBottomBar
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.topbar.RemasterTopBar
import com.vgleadsheets.topbar.TopBarViewModel
import com.vgleadsheets.ui.list.listScreenEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemasterAppUi(
    modifier: Modifier
) {
    val navController = rememberNavController()

    val topBarViewModel: TopBarViewModel = hiltViewModel()

    val titleUpdater = { title: TitleBarModel ->
        topBarViewModel.updateTitle(title)
    }

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val state by topBarViewModel.uiState.collectAsState()
            RemasterTopBar(
                state,
                scrollBehavior,
            ) { action ->
                topBarViewModel.handleAction(action)
            }
        },
        bottomBar = {
            RemasterBottomBar(navController)
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            startDestination = Destination.HOME.template()
        ) {
            val globalModifier = Modifier.fillMaxSize()
            val navigationAction = { linkUri: String ->
                navController.navigate(linkUri)
            }

            Destination.entries.forEach { destination ->
                listScreenEntry(
                    destination = destination,
                    navigateTo = navigationAction,
                    titleUpdater = titleUpdater,
                    topBarState = topBarState,
                    globalModifier = globalModifier
                )
            }
        }
    }
}
