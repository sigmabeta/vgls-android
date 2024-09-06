package com.vgleadsheets.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.bottombar.BottomBarState
import com.vgleadsheets.bottombar.BottomBarViewModel
import com.vgleadsheets.bottombar.RemasterBottomBar
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.nav.NavState
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.nav.SystemUiVisibility
import com.vgleadsheets.search.searchScreenNavEntry
import com.vgleadsheets.topbar.RemasterTopBar
import com.vgleadsheets.topbar.TopBarState
import com.vgleadsheets.topbar.TopBarViewModel
import com.vgleadsheets.ui.licenses.licensesScreenNavEntry
import com.vgleadsheets.ui.list.listScreenEntry
import com.vgleadsheets.ui.viewer.viewerScreenNavEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemasterAppUi(
    showSystemBars: () -> Unit,
    hideSystemBars: () -> Unit,
    modifier: Modifier
) {
    val navController = rememberNavController()

    val snackbarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    val navViewModel = hiltViewModel<NavViewModel>()

    navViewModel.navController = navController
    navViewModel.snackbarScope = snackbarScope
    navViewModel.snackbarHostState = snackbarHostState

    LaunchedEffect(Unit) { navViewModel.startWatchingBackstack() }

    val navState by navViewModel.uiState.collectAsState()
    handleSystemBars(navState, showSystemBars, hideSystemBars)

    val topBarViewModel: TopBarViewModel = hiltViewModel()
    val topBarVmState by topBarViewModel.uiState.collectAsState()
    val topBarActionHandler = remember { { action: VglsAction -> topBarViewModel.sendAction(action) } }

    val bottomBarViewModel: BottomBarViewModel = hiltViewModel()
    val bottomBarVmState by bottomBarViewModel.uiState.collectAsState()

    AppContent(
        navController = navController,
        scrollBehavior = scrollBehavior,
        snackbarHostState = snackbarHostState,
        topBarVmState = topBarVmState,
        topBarActionHandler = topBarActionHandler,
        bottomBarVmState = bottomBarVmState,
        mainContent = { innerPadding -> MainContent(navController, innerPadding, topBarState) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    snackbarHostState: SnackbarHostState,
    topBarVmState: TopBarState,
    topBarActionHandler: (VglsAction) -> Unit,
    bottomBarVmState: BottomBarState,
    mainContent: @Composable (PaddingValues) -> Unit,
    modifier: Modifier,
) {
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopBarContent(topBarVmState, scrollBehavior, topBarActionHandler) },
        bottomBar = { BottomBarContent(bottomBarVmState, navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = mainContent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    navController: NavHostController,
    innerPadding: PaddingValues,
    topBarState: TopAppBarState
) {
    NavHost(
        navController = navController,
        modifier = Modifier.padding(innerPadding),
        startDestination = Destination.HOME.template(),
        enterTransition = { enterTransition() },
        exitTransition = { exitTransition() },
        popEnterTransition = { popEnterTransition() },
        popExitTransition = { popExitTransition() },
    ) {
        val globalModifier = Modifier.fillMaxSize()

        Destination.entries.forEach { destination ->
            if (!destination.isImplemented) {
                return@forEach
            }

            listScreenEntry(
                destination = destination,
                topBarState = topBarState,
                globalModifier = globalModifier,
            )
        }

        searchScreenNavEntry(
            topBarState = topBarState,
            globalModifier = globalModifier,
        )

        viewerScreenNavEntry(
            topBarState = topBarState,
            globalModifier = globalModifier,
        )

        licensesScreenNavEntry(
            topBarState = topBarState,
            globalModifier = globalModifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarContent(
    state: TopBarState,
    scrollBehavior: TopAppBarScrollBehavior,
    handleAction: (VglsAction) -> Unit,
) {
    RemasterTopBar(
        state = state,
        scrollBehavior = scrollBehavior,
        handleAction = handleAction,
    )
}

@Composable
private fun BottomBarContent(
    state: BottomBarState,
    navController: NavController,
) {
    RemasterBottomBar(
        state = state,
        navController = navController,
    )
}

private fun handleSystemBars(
    navState: NavState,
    showSystemBars: () -> Unit,
    hideSystemBars: () -> Unit
) {
    when (navState.visibility) {
        SystemUiVisibility.VISIBLE -> showSystemBars()
        SystemUiVisibility.HIDDEN -> hideSystemBars()
    }
}
