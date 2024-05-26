package com.vgleadsheets.scaffold

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.bottombar.RemasterBottomBar
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.topbar.RemasterTopBar
import com.vgleadsheets.topbar.TopBarViewModel
import com.vgleadsheets.topbar.topBarViewModel
import com.vgleadsheets.ui.list.listScreenEntry
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemasterAppUi(
    modifier: Modifier
) {
    val navController = rememberNavController()

    val snackbarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val navFunction = { destination: String -> navController.navigate(destination) }
    val navBack: () -> Unit = { navController.popBackStack() }

    val launchSnackbar: (VglsEvent.ShowSnackbar) -> Unit = { snackbarEvent ->
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
                SnackbarResult.ActionPerformed -> sink.sendAction(VglsAction.SnackbarActionClicked(actionDetails.clickAction))
                SnackbarResult.Dismissed -> sink.sendAction(VglsAction.SnackbarDismissed(actionDetails.clickAction))
            }
        }
    }

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    val eventDispatcher = remember { EventDispatcher(navFunction, navBack, launchSnackbar) }

    val topBarViewModel: TopBarViewModel = topBarViewModel(eventDispatcher)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val state by topBarViewModel.uiState.collectAsState()
            RemasterTopBar(
                state = state,
                scrollBehavior = scrollBehavior,
                handleAction = { action -> topBarViewModel.sendAction(action) },
            )
        },
        bottomBar = {
            RemasterBottomBar(navController)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
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
        }
    }
}
