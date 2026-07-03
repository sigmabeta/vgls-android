package com.vgleadsheets.composables

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

@OptIn(ExperimentalTextApi::class)
actual fun condensedSansSerifFontFamily(): FontFamily = FontFamily(
    Font(DeviceFontFamilyName("sans-serif-condensed")),
)
