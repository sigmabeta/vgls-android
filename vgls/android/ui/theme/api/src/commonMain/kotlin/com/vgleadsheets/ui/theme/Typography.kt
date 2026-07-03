package com.vgleadsheets.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import com.vgleadsheets.ui.theme.tokens.VglsTypographyTokens

/*
* Build a Material3 [Typography] from Vgls's type-scale tokens (sizes/weights/spacing) with the
* caller-supplied brand [FontFamily]. Display/Headline/TitleLarge use the brand face (MuseJazz);
* the rest use the platform sans-serif. The font family is applied here — not baked into the
* tokens — because the brand face is a Compose Multiplatform resource loaded in a composable
* (see :vgls:android:ui:fonts:real + AppTheme).
*/
fun vglsTypography(brand: FontFamily): Typography {
    val plain = FontFamily.SansSerif
    return Typography(
        displayLarge = VglsTypographyTokens.DisplayLarge.copy(fontFamily = brand),
        displayMedium = VglsTypographyTokens.DisplayMedium.copy(fontFamily = brand),
        displaySmall = VglsTypographyTokens.DisplaySmall.copy(fontFamily = brand),
        headlineLarge = VglsTypographyTokens.HeadlineLarge.copy(fontFamily = brand),
        headlineMedium = VglsTypographyTokens.HeadlineMedium.copy(fontFamily = brand),
        headlineSmall = VglsTypographyTokens.HeadlineSmall.copy(fontFamily = brand),
        titleLarge = VglsTypographyTokens.TitleLarge.copy(fontFamily = brand),
        titleMedium = VglsTypographyTokens.TitleMedium.copy(fontFamily = plain),
        titleSmall = VglsTypographyTokens.TitleSmall.copy(fontFamily = plain),
        bodyLarge = VglsTypographyTokens.BodyLarge.copy(fontFamily = plain),
        bodyMedium = VglsTypographyTokens.BodyMedium.copy(fontFamily = plain),
        bodySmall = VglsTypographyTokens.BodySmall.copy(fontFamily = plain),
        labelLarge = VglsTypographyTokens.LabelLarge.copy(fontFamily = plain),
        labelMedium = VglsTypographyTokens.LabelMedium.copy(fontFamily = plain),
        labelSmall = VglsTypographyTokens.LabelSmall.copy(fontFamily = plain),
    )
}
