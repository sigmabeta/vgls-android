package com.vgleadsheets.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventDispatcherReal
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.bottombar.BottomBarVisibility
import com.vgleadsheets.bottombar.RemasterBottomBar
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.topbar.RemasterTopBar
import com.vgleadsheets.topbar.TopBarState
import com.vgleadsheets.topbar.topBarViewModel
import com.vgleadsheets.ui.list.listScreenEntry
import com.vgleadsheets.ui.viewer.viewerScreenNavEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemasterAppUi(
    modifier: Modifier
) {
    val navController = rememberNavController()

    val snackbarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val navFunction = remember { { destination: String -> navController.navigate(destination) } }
    val navBack: () -> Unit = remember { { navController.popBackStack() } }

    val launchSnackbar = remember { createSnackbarLauncher(snackbarScope, snackbarHostState) }

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    var bottomBarVisibility by remember { mutableStateOf(BottomBarVisibility.VISIBLE) }
    val showBottomBar = remember { { bottomBarVisibility = BottomBarVisibility.VISIBLE } }
    val hideBottomBar = remember { { bottomBarVisibility = BottomBarVisibility.HIDDEN } }

    val eventDispatcher = remember { EventDispatcherReal(navFunction, navBack, launchSnackbar, showBottomBar, hideBottomBar) }

    val topBarViewModel = topBarViewModel(eventDispatcher)
    val topBarVmState by topBarViewModel.uiState.collectAsState()
    val topBarActionHandler = remember { { action: VglsAction -> topBarViewModel.sendAction(action) } }

    AppContent(
        navController = navController,
        scrollBehavior = scrollBehavior,
        snackbarHostState = snackbarHostState,
        topBarVmState = topBarVmState,
        topBarActionHandler = topBarActionHandler,
        bottomBarVisibility = bottomBarVisibility,
        mainContent =  { innerPadding -> MainContent(navController, innerPadding, eventDispatcher, topBarState) },
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
    bottomBarVisibility: BottomBarVisibility,
    mainContent: @Composable (PaddingValues) -> Unit,
    modifier: Modifier,
) {
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopBarContent(topBarVmState, scrollBehavior, topBarActionHandler) },
        bottomBar = { BottomBarContent(navController, bottomBarVisibility) },
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
    eventDispatcher: EventDispatcher,
    topBarState: TopAppBarState
) {
    NavHost(
        navController = navController,
        modifier = Modifier.padding(innerPadding),
        startDestination = Destination.HOME.template()
    ) {
        val globalModifier = Modifier.fillMaxSize()

        Destination.entries.forEach { destination ->
            if (!destination.isImplemented) {
                return@forEach
            }

            listScreenEntry(
                destination = destination,
                eventDispatcher = eventDispatcher,
                topBarState = topBarState,
                globalModifier = globalModifier,
            )
        }

        viewerScreenNavEntry(
            eventDispatcher = eventDispatcher,
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
    navController: NavController,
    visibility: BottomBarVisibility
) {
    RemasterBottomBar(navController, visibility)
}

private fun createSnackbarLauncher(
    snackbarScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
): (VglsEvent.ShowSnackbar) -> Unit = { snackbarEvent ->
    snackbarScope.launch {
        val actionDetails = snackbarEvent.actionDetails
        val result = snackbarHostState.showSnackbar(
            message = snackbarEvent.message,
            actionLabel = actionDetails?.clickActionLabel,
            withDismissAction = snackbarEvent.withDismissAction,
            duration = SnackbarDuration.Short
        )

        if (actionDetails == null) {
            return@launch
        }

        val sink = actionDetails.actionSink

        when (result) {
            SnackbarResult.ActionPerformed -> {
                sink.sendAction(VglsAction.SnackbarActionClicked(actionDetails.clickAction))
            }

            SnackbarResult.Dismissed -> {
                sink.sendAction(VglsAction.SnackbarDismissed(actionDetails.clickAction))
            }
        }
    }
}
