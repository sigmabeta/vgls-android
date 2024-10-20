package com.vgleadsheets.scaffold

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowHeightSizeClass
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.bottombar.NavBarItem
import com.vgleadsheets.bottombar.NavBarState
import com.vgleadsheets.bottombar.NavBarViewModel
import com.vgleadsheets.list.WidthClass
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.nav.NavState
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.nav.SystemUiVisibility
import com.vgleadsheets.search.searchScreenNavEntry
import com.vgleadsheets.topbar.TopBarViewModel
import com.vgleadsheets.ui.licenses.licensesScreenNavEntry
import com.vgleadsheets.ui.list.listScreenEntry
import com.vgleadsheets.ui.viewer.viewerScreenNavEntry

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("MaxLineLength")
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

    val topBarExpander = remember {
        {
            topBarState.heightOffset = 0.0f
            topBarState.contentOffset = 0.0f
        }
    }

    val navViewModel = hiltViewModel<NavViewModel>()

    navViewModel.navController = navController
    navViewModel.snackbarScope = snackbarScope
    navViewModel.snackbarHostState = snackbarHostState
    navViewModel.topBarExpander = topBarExpander

    val navState by navViewModel.uiState.collectAsState()
    handleSystemBars(navState, showSystemBars, hideSystemBars)

    val topBarViewModel: TopBarViewModel = hiltViewModel()
    val topBarVmState by topBarViewModel.uiState.collectAsState()
    val topBarConfig = TopBarConfig(
        state = topBarVmState,
        behavior = scrollBehavior,
        handleAction = remember { { action: VglsAction -> topBarViewModel.sendAction(action) } },
    )

    val navBarViewModel: NavBarViewModel = hiltViewModel()
    val bottomBarVmState by navBarViewModel.uiState.collectAsState()

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val displayHeightClass = adaptiveInfo.windowSizeClass.windowHeightSizeClass

    val actualModifier = if (displayHeightClass == WindowHeightSizeClass.COMPACT) {
        modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    } else {
        modifier
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    AppContent(
        topBarConfig = topBarConfig,
        navBarState = bottomBarVmState,
        navEventSink = navViewModel,
        currentRoute = currentRoute,
        snackbarHostState = snackbarHostState,
        modifier = actualModifier,
        screen = { innerPadding, widthClass ->
            NavHostAndSuch(navController, innerPadding, widthClass)
        },
    )
}

@Composable
fun AppContent(
    topBarConfig: TopBarConfig,
    navBarState: NavBarState,
    navEventSink: EventSink,
    currentRoute: String?,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier,
    syntheticWidthClass: WidthClass? = null,
    screen: @Composable (PaddingValues, WidthClass) -> Unit,
) {
    val adaptiveInfo = syntheticWidthClass?.toAdaptiveInfoSynthetic() ?: currentWindowAdaptiveInfo()

    val orientation = LocalConfiguration.current.orientation
    val layoutType = calculateNavSuiteType(adaptiveInfo, orientation)

    val displayWidthClass = adaptiveInfo.windowSizeClass.windowWidthSizeClass.toWidthClass()

    VglsNavSuiteScaffold(
        layoutType = layoutType,
        navSuiteItems = navSuiteItems(currentRoute, navEventSink),
        topBarConfig = topBarConfig,
        navBarState = navBarState,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        screen = { innerPadding -> screen(innerPadding, displayWidthClass) },
    )
}

@Composable
private fun NavHostAndSuch(
    navController: NavHostController,
    innerPadding: PaddingValues,
    displayWidthClass: WidthClass,
) {
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = remember {
        { enterTransition() }
    }
    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = remember {
        { exitTransition() }
    }
    val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = remember {
        { popEnterTransition() }
    }
    val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = remember {
        { popExitTransition() }
    }

    NavHost(
        navController = navController,
        modifier = Modifier.padding(innerPadding),
        startDestination = Destination.HOME.template(),
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
    ) {
        val globalModifier = Modifier.fillMaxSize()

        Destination.entries.forEach { destination ->
            if (!destination.isImplemented) {
                return@forEach
            }

            listScreenEntry(
                destination = destination,
                displayWidthClass = displayWidthClass,
                globalModifier = globalModifier,
            )
        }

        searchScreenNavEntry(
            globalModifier = globalModifier,
        )

        viewerScreenNavEntry(
            globalModifier = globalModifier,
        )

        licensesScreenNavEntry(
            globalModifier = globalModifier,
        )
    }
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

@Composable
fun navSuiteItems(
    currentRoute: String?,
    navEventSink: EventSink,
): NavigationSuiteScope.() -> Unit = {
    NavBarItem.entries.forEach { navItem ->
        item(
            icon = { Icon(navItem.icon, contentDescription = navItem.name) },
            label = { Text(navItem.label) },
            selected = currentRoute == navItem.route,
            onClick = {
                navEventSink.sendEvent(
                    VglsEvent.NavigateSingleTopLevel(navItem.route, "NavBar")
                )
            }
        )
    }
}
