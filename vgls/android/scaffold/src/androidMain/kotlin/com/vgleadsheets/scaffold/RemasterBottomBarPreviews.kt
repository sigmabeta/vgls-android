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
import com.vgleadsheets.ui.theme.AppTheme

@Preview
@Composable
private fun PreviewLight() {
    AppTheme {
        PreviewContent()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    AppTheme {
        PreviewContent()
    }
}

@Composable
private fun PreviewContent() {
    val navItemProvider by rememberStateOfItems(
        navSuiteItems(
            currentRoute = null,
            shouldBeNavRail = false,
            navEventSink = { }
        )
    )
    RemasterBottomBar(
        NavBarState(),
        NavigationSuiteType.NavigationBar,
        navItemProvider,
    )
}
