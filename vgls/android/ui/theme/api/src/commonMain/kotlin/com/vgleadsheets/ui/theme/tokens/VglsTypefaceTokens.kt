package com.vgleadsheets.ui.theme.tokens

import androidx.compose.ui.text.font.FontWeight

// Font families are no longer baked in here — they're applied at theme-build time by
// `vglsTypography(brand)` (the brand face comes from :vgls:android:ui:fonts:real, loaded in a
// composable). Only the weight tokens remain.
internal object VglsTypefaceTokens {
    val WeightBold = FontWeight.Bold
    val WeightMedium = FontWeight.Medium
    val WeightRegular = FontWeight.Normal
}
