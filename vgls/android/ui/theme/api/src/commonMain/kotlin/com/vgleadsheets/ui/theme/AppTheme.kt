package com.vgleadsheets.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.ui.fonts.museJazzFontFamily

// Inlines the (Android-only) sage SageMaterial/SageMaterialMenu helpers so the theme can live in
// commonMain — both were thin MaterialTheme wrappers.
//
// [darkTheme] is tri-state to mirror the persisted `ThemeMode`: true pins dark, false pins light,
// and null follows the system setting.
@Composable
fun AppTheme(
    darkTheme: Boolean? = null,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme ?: isSystemInDarkTheme()) VglsDark else VglsLight

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
