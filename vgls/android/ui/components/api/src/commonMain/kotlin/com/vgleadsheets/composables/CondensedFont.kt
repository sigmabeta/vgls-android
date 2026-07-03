package com.vgleadsheets.composables

import androidx.compose.ui.text.font.FontFamily

/**
 * A condensed sans-serif for section headers. Android maps this to the system "sans-serif-condensed"
 * device font ([androidx.compose.ui.text.font.DeviceFontFamilyName], an android-only API); other
 * targets fall back to the platform sans-serif since that device font isn't available off-Android.
 */
expect fun condensedSansSerifFontFamily(): FontFamily
