package com.vgleadsheets.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

// Inlines the (Android-only) sage SageMaterial/SageMaterialMenu helpers so the theme can live in
// commonMain — both were thin MaterialTheme wrappers.
@Composable
fun AppTheme(
    forceDark: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colors = if (!isSystemInDarkTheme() && !forceDark) VglsLight else VglsDark

    MaterialTheme(
        typography = VglsTypography,
        colorScheme = colors,
        content = content,
    )
}

@Composable
fun AppThemeMenu(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        typography = VglsTypography,
        colorScheme = VglsMenu,
        content = content,
    )
}
