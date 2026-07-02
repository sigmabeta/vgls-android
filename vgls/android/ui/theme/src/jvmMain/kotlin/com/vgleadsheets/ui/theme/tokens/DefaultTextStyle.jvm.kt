package com.vgleadsheets.ui.theme.tokens

import androidx.compose.ui.text.TextStyle

// JVM/desktop (Skia/AWT) has no legacy includeFontPadding knob, so the base style is just default.
internal actual fun vglsDefaultTextStyle(): TextStyle = TextStyle.Default
