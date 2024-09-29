package com.vgleadsheets.scaffold

import android.content.res.Configuration
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.calculatePosture
import androidx.compose.material3.adaptive.collectFoldingFeaturesAsState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.core.layout.WindowWidthSizeClass.Companion.COMPACT
import androidx.window.core.layout.WindowWidthSizeClass.Companion.EXPANDED
import androidx.window.core.layout.WindowWidthSizeClass.Companion.MEDIUM
import com.vgleadsheets.list.WidthClass

fun calculateNavSuiteType(
    adaptiveInfo: WindowAdaptiveInfo,
    orientation: Int
): NavigationSuiteType {
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        return NavigationSuiteType.NavigationRail
    }

    return NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
}

fun WindowWidthSizeClass.toWidthClass() = when (this) {
    COMPACT -> WidthClass.COMPACT
    MEDIUM -> WidthClass.MEDIUM
    EXPANDED -> WidthClass.EXPANDED
    else -> throw IllegalArgumentException("Invalid $this")
}

fun WidthClass.toWidthClass() = when (this) {
    WidthClass.COMPACT -> COMPACT
    WidthClass.MEDIUM -> MEDIUM
    WidthClass.EXPANDED -> EXPANDED
    else -> throw IllegalArgumentException("Invalid WidthClass: $this")
}

fun WidthClass.toWidthDpSynthetic() = when (this) {
    WidthClass.COMPACT -> 400
    WidthClass.MEDIUM -> 700
    WidthClass.EXPANDED -> 900
    else -> throw IllegalArgumentException("Invalid WidthClass: $this")
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun WidthClass.toAdaptiveInfoSynthetic(): WindowAdaptiveInfo {
    return WindowAdaptiveInfo(
        WindowSizeClass.compute(toWidthDpSynthetic().toFloat(), 0f),
        calculatePosture(collectFoldingFeaturesAsState().value)
    )
}

@Composable
fun currentWindowWidthClassSynthetic() = currentWindowAdaptiveInfo()
    .windowSizeClass
    .windowWidthSizeClass
    .toWidthClass()
