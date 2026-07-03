package com.vgleadsheets.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.ui.fonts.museJazzFontFamily

// Inlines the (Android-only) sage SageMaterial/SageMaterialMenu helpers so the theme can live in
// commonMain — both were thin MaterialTheme wrappers.
@Composable
fun AppTheme(
    forceDark: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colors = if (!isSystemInDarkTheme() && !forceDark) VglsLight else VglsDark

    MaterialTheme(
        typography = vglsTypography(brand = museJazzFontFamily()),
        colorScheme = colors,
        content = content,
    )
}

@Composable
fun AppThemeMenu(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        typography = vglsTypography(brand = museJazzFontFamily()),
        colorScheme = VglsMenu,
        content = content,
    )
}
