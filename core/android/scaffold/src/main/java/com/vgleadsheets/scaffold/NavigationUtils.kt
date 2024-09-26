package com.vgleadsheets.scaffold

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection

fun calculateNavSuiteType(
    adaptiveInfo: WindowAdaptiveInfo,
    orientation: Int
): NavigationSuiteType {
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        return NavigationSuiteType.NavigationRail
    }

    return NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
}

@Composable
fun calculateCutoutInset(): Modifier {
    val cutoutInsets = WindowInsets.displayCutout
    val leftInsetOnly = WindowInsets(
        left = cutoutInsets.getLeft(
            LocalDensity.current,
            LocalLayoutDirection.current
        )
    )

    return Modifier.windowInsetsPadding(leftInsetOnly)
}
