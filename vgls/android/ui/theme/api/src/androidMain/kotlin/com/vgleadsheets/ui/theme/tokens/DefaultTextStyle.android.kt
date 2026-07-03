package com.vgleadsheets.ui.theme.tokens

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle

private val DefaultPlatformTextStyle = PlatformTextStyle(
    includeFontPadding = false,
)

internal actual fun vglsDefaultTextStyle(): TextStyle = TextStyle.Default.copy(platformStyle = DefaultPlatformTextStyle)
