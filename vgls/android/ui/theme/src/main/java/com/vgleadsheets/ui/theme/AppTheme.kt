package com.vgleadsheets.ui.theme

import androidx.compose.runtime.Composable
import net.sigmabeta.sage.ui.themes.SageMaterial
import net.sigmabeta.sage.ui.themes.SageMaterialMenu

@Composable
fun AppTheme(
    forceDark: Boolean = false,
    content: @Composable () -> Unit,
) {
    SageMaterial(
        lightColors = VglsLight,
        darkColors = VglsDark,
        typography = VglsTypography,
        forceDark = forceDark,
        content = content,
    )
}

@Composable
fun AppThemeMenu(
    content: @Composable () -> Unit,
) {
    SageMaterialMenu(
        menuColors = VglsMenu,
        typography = VglsTypography,
        content = content,
    )
}
