package com.vgleadsheets.ui.theme.tokens

import androidx.compose.ui.text.font.FontFamily

// The brand face (MuseJazz) is an Android font resource today, so it's a platform seam: Android
// supplies the real FontFamily from R.font; JVM/desktop stubs to the default until the .otf is
// moved to compose-multiplatform resources.
internal expect val museJazzFontFamily: FontFamily

internal object VglsFonts {
    val MuseJazz = museJazzFontFamily
}
