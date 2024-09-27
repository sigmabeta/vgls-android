package com.vgleadsheets.scaffold

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.bottombar.NavBarState
import com.vgleadsheets.bottombar.NavBarVisibility
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
internal fun RemasterBottomBar(
    state: NavBarState,
    layoutType: NavigationSuiteType,
    navItemProvider: NavigationSuiteItemProvider,
) {
    val shouldNavBeVisible = state.visibility == NavBarVisibility.VISIBLE
    val shouldNavBeBar = layoutType == NavigationSuiteType.NavigationBar

    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        visible = shouldNavBeVisible && shouldNavBeBar,
        label = "BottomBarVisibility"
    ) {
        val colors: NavigationSuiteColors = NavigationSuiteDefaults.colors()
        val defaultItemColors = NavigationSuiteDefaults.itemColors()

        NavigationBar(
            containerColor = colors.navigationBarContainerColor,
            contentColor = colors.navigationBarContentColor,
        ) {
            navItemProvider.itemList.forEach {
                NavigationBarItem(
                    modifier = it.modifier,
                    selected = it.selected,
                    onClick = it.onClick,
                    icon = { NavigationItemIcon(icon = it.icon, badge = it.badge) },
                    enabled = it.enabled,
                    label = it.label,
                    alwaysShowLabel = it.alwaysShowLabel,
                    colors = it.colors?.navigationBarItemColors
                        ?: defaultItemColors.navigationBarItemColors,
                    interactionSource = it.interactionSource
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLight() {
    VglsMaterial {
        PreviewContent()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    VglsMaterial {
        PreviewContent()
    }
}

@Composable
private fun PreviewContent() {
    val navItemProvider by rememberStateOfItems(navSuiteItems(currentRoute = null))
    RemasterBottomBar(
        NavBarState(NavBarVisibility.VISIBLE),
        NavigationSuiteType.NavigationBar,
        navItemProvider,
    )
}
