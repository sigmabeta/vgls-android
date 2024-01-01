package com.vgleadsheets.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vgleadsheets.bottombar.RemasterBottomBar
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.composables.NameCaptionListItem
import com.vgleadsheets.topbar.RemasterTopBar
import com.vgleadsheets.topbar.TopBarViewModel

@Composable
fun RemasterScreen(
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
                NameCaptionListItem(
                    model = NameCaptionListModel(
                        dataId = 1234L,
                        name = "Just a home screen",
                        caption = "Nothing to see here",
                        onClick = { }
                    ),
                    modifier = Modifier
                )
            }
        }
    }
}
