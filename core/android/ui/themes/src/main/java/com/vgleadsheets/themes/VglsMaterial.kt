package com.vgleadsheets.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.colors.VglsDark
import com.vgleadsheets.colors.VglsLight

@Composable
fun VglsMaterial(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!useDarkTheme) {
        VglsLight
    } else {
        VglsDark
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
