package com.vgleadsheets.scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vgleadsheets.bottombar.NavBarItem
import com.vgleadsheets.bottombar.NavBarState
import com.vgleadsheets.bottombar.NavBarVisibility
import com.vgleadsheets.topbar.RemasterTopBar
import net.sigmabeta.sage.appcomm.EventSink
import net.sigmabeta.sage.appcomm.SageEvent
import com.vgleadsheets.strings.id
import net.sigmabeta.sage.ui.vector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("MaxLineLength")
fun VglsNavSuiteScaffold(
    layoutType: NavigationSuiteType,
    currentRoute: String?,
    navEventSink: EventSink,
    topBarConfig: TopBarConfig,
    navBarState: NavBarState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    screen: @Composable (PaddingValues) -> Unit = {},
) {
    val colors: NavigationSuiteColors = NavigationSuiteDefaults.colors()
    val defaultItemColors = NavigationSuiteDefaults.itemColors()

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        val shouldNavBeVisible = navBarState.visibility == NavBarVisibility.VISIBLE
        val shouldNavBeRail = layoutType == NavigationSuiteType.NavigationRail
        val navSuiteItems = navSuiteItems(shouldNavBeRail, currentRoute, navEventSink)
        val navItemProvider by rememberStateOfItems(navSuiteItems)

        AnimatedVisibility(
            visible = shouldNavBeVisible && shouldNavBeRail,
            label = "NavRailVisibility"
        ) {
            NavigationRail(
                containerColor = colors.navigationRailContainerColor,
                contentColor = colors.navigationRailContentColor,
                modifier = modifier.safeDrawingPadding(),
            ) {
                navItemProvider.itemList.forEach {
                    NavigationRailItem(
                        modifier = it.modifier,
                        selected = it.selected,
                        onClick = it.onClick,
                        icon = { NavigationItemIcon(icon = it.icon, badge = it.badge) },
                        enabled = it.enabled,
                        label = it.label,
                        alwaysShowLabel = it.alwaysShowLabel,
                        colors =
                        it.colors?.navigationRailItemColors
                            ?: defaultItemColors.navigationRailItemColors,
                        interactionSource = it.interactionSource
                    )
                }
            }
        }

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = { RemasterTopBar(state = topBarConfig.state, scrollBehavior = topBarConfig.behavior, handleAction = topBarConfig.handleAction) },
            bottomBar = { RemasterBottomBar(state = navBarState, layoutType = layoutType, navItemProvider = navItemProvider) },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            content = { innerPadding -> screen(innerPadding) },
            modifier = modifier,
        )
    }
}

@Composable
fun navSuiteItems(
    shouldBeNavRail: Boolean,
    currentRoute: String?,
    navEventSink: EventSink,
): NavigationSuiteScope.() -> Unit = {
    val items = if (shouldBeNavRail) {
        NavBarItem.RAIL_ITEMS
    } else {
        NavBarItem.BOTTOM_BAR_ITEMS
    }

    items.forEach { navItem ->
        item(
            icon = {
                val label = stringResource(navItem.labelId.id())
                Icon(navItem.icon.vector(), contentDescription = label)
            },
            label = {
                val label = stringResource(navItem.labelId.id())
                Text(label)
            },
            selected = currentRoute == navItem.route,
            onClick = {
                navEventSink.sendEvent(
                    SageEvent.NavigateSingleTopLevel(navItem.route, "NavBar")
                )
            }
        )
    }
}
