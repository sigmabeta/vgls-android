package com.vgleadsheets.scaffold

import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.sigmabeta.sage.list.WidthClass

// Material3 window width breakpoints, as plain Dp thresholds. This replaces the base
// material3-adaptive `currentWindowAdaptiveInfo()` / `WindowSizeClass` (which have no multiplatform
// twin in the catalog) with the cross-platform `LocalWindowInfo.containerSize` — the same
// BoxWithConstraints/maxWidth approach Chipbox uses for its NavigationSuite layout type.
private val WIDTH_MEDIUM = 600.dp
private val WIDTH_EXPANDED = 840.dp

fun widthClassFromDp(widthDp: Dp): WidthClass = when {
    widthDp < WIDTH_MEDIUM -> WidthClass.COMPACT
    widthDp < WIDTH_EXPANDED -> WidthClass.MEDIUM
    else -> WidthClass.EXPANDED
}

/** Compact windows get a bottom bar; wider windows get a nav rail. */
fun navSuiteTypeFor(widthClass: WidthClass): NavigationSuiteType =
    if (widthClass == WidthClass.COMPACT) {
        NavigationSuiteType.NavigationBar
    } else {
        NavigationSuiteType.NavigationRail
    }

/** The current window's [WidthClass], derived from the multiplatform [LocalWindowInfo]. */
@Composable
fun currentWindowWidthClassSynthetic(): WidthClass {
    val windowInfo = LocalWindowInfo.current
    val widthDp = with(LocalDensity.current) { windowInfo.containerSize.width.toDp() }
    return widthClassFromDp(widthDp)
}
