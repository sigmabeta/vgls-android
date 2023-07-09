package com.vgleadsheets.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.ui.colors.VglsDark
import com.vgleadsheets.ui.colors.VglsLight
import com.vgleadsheets.ui.colors.VglsMenu

@Composable
fun VglsMaterial(
    content: @Composable () -> Unit
) {
    val colors = if (!isSystemInDarkTheme()) {
        VglsLight
    } else {
        VglsDark
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

@Composable
fun VglsMaterialMenu(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = VglsMenu,
        content = content
    )
}
