package com.vgleadsheets.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.window.core.layout.WindowHeightSizeClass
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.vgleadsheets.bottombar.NavBarState
import com.vgleadsheets.bottombar.NavBarViewModel
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.nav.RoutedScreen
import com.vgleadsheets.topbar.TopBarViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel
import net.sigmabeta.sage.appcomm.EventSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.list.WidthClass
import com.vgleadsheets.nav.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("MaxLineLength")
@Composable
fun RemasterAppUi(
    modifier: Modifier
) {
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

    val navViewModel = metroViewModel<NavViewModel>()

    navViewModel.snackbarScope = snackbarScope
    navViewModel.snackbarHostState = snackbarHostState
    navViewModel.topBarExpander = topBarExpander

    // Current route for nav-rail selection — updated from the Voyager navigator's top screen below.
    var currentRoute by remember { mutableStateOf(Destination.HOME.noArgs()) }

    val topBarViewModel: TopBarViewModel = metroViewModel()
    val topBarVmState by topBarViewModel.uiState.collectAsState()
    val topBarConfig = TopBarConfig(
        state = topBarVmState,
        behavior = scrollBehavior,
        handleAction = remember { { action: SageAction -> topBarViewModel.sendAction(action) } },
    )

    val navBarViewModel: NavBarViewModel = metroViewModel()
    val bottomBarVmState by navBarViewModel.uiState.collectAsState()

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val displayHeightClass = adaptiveInfo.windowSizeClass.windowHeightSizeClass

    val actualModifier = if (displayHeightClass == WindowHeightSizeClass.COMPACT) {
        modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    } else {
        modifier
    }

    AppContent(
        topBarConfig = topBarConfig,
        navBarState = bottomBarVmState,
        navEventSink = navViewModel,
        currentRoute = currentRoute,
        snackbarHostState = snackbarHostState,
        modifier = actualModifier,
        screen = { innerPadding, widthClass ->
            VoyagerNavHost(
                innerPadding = innerPadding,
                displayWidthClass = widthClass,
                navViewModel = navViewModel,
                onRouteChange = { currentRoute = it },
            )
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
        currentRoute = currentRoute,
        navEventSink = navEventSink,
        topBarConfig = topBarConfig,
        navBarState = navBarState,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        screen = { innerPadding -> screen(innerPadding, displayWidthClass) },
    )
}

@Composable
private fun VoyagerNavHost(
    innerPadding: PaddingValues,
    displayWidthClass: WidthClass,
    navViewModel: NavViewModel,
    onRouteChange: (String) -> Unit,
) {
    // Voyager owns the back stack now (replacing the AndroidX NavHost). Screens read the width class
    // off LocalDisplayWidthClass since Screen.Content() takes no params. Voyager's own back handling
    // is disabled (onBackPressed = { false }) because each screen's BackHandler already routes
    // DeviceBack -> SageEvent.NavigateBack -> NavViewModel -> navigator.pop().
    CompositionLocalProvider(LocalDisplayWidthClass provides displayWidthClass) {
        Navigator(HomeScreen, onBackPressed = { false }) { navigator ->
            navViewModel.navigator = navigator
            navViewModel.screenForRoute = ::screenForRoute

            val topRoute = (navigator.lastItem as? RoutedScreen)?.route
            LaunchedEffect(topRoute) {
                if (topRoute != null) onRouteChange(topRoute)
            }

            SlideTransition(
                navigator = navigator,
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
            )
        }
    }
}
