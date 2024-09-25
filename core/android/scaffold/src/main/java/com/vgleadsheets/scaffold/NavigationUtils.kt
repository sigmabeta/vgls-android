package com.vgleadsheets.scaffold

import android.content.res.Configuration
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType

fun calculateNavSuiteType(
    adaptiveInfo: WindowAdaptiveInfo,
    orientation: Int
): NavigationSuiteType {
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        return NavigationSuiteType.NavigationRail
    }

    return NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
}
