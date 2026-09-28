package com.vgleadsheets.ui.theme.tokens

import androidx.compose.ui.text.TextStyle

internal object VglsTypographyTokens {
    val BodyLarge =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.BodyLargeWeight,
            fontSize = VglsTypeScaleTokens.BodyLargeSize,
            lineHeight = VglsTypeScaleTokens.BodyLargeLineHeight,
            letterSpacing = VglsTypeScaleTokens.BodyLargeTracking,
        )
    val BodyMedium =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.BodyMediumWeight,
            fontSize = VglsTypeScaleTokens.BodyMediumSize,
            lineHeight = VglsTypeScaleTokens.BodyMediumLineHeight,
            letterSpacing = VglsTypeScaleTokens.BodyMediumTracking,
        )
    val BodySmall =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.BodySmallWeight,
            fontSize = VglsTypeScaleTokens.BodySmallSize,
            lineHeight = VglsTypeScaleTokens.BodySmallLineHeight,
            letterSpacing = VglsTypeScaleTokens.BodySmallTracking,
        )
    val DisplayLarge =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.DisplayLargeWeight,
            fontSize = VglsTypeScaleTokens.DisplayLargeSize,
            lineHeight = VglsTypeScaleTokens.DisplayLargeLineHeight,
            letterSpacing = VglsTypeScaleTokens.DisplayLargeTracking,
        )
    val DisplayMedium =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.DisplayMediumWeight,
            fontSize = VglsTypeScaleTokens.DisplayMediumSize,
            lineHeight = VglsTypeScaleTokens.DisplayMediumLineHeight,
            letterSpacing = VglsTypeScaleTokens.DisplayMediumTracking,
        )
    val DisplaySmall =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.DisplaySmallWeight,
            fontSize = VglsTypeScaleTokens.DisplaySmallSize,
            lineHeight = VglsTypeScaleTokens.DisplaySmallLineHeight,
            letterSpacing = VglsTypeScaleTokens.DisplaySmallTracking,
        )
    val HeadlineLarge =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.HeadlineLargeWeight,
            fontSize = VglsTypeScaleTokens.HeadlineLargeSize,
            lineHeight = VglsTypeScaleTokens.HeadlineLargeLineHeight,
            letterSpacing = VglsTypeScaleTokens.HeadlineLargeTracking,
        )
    val HeadlineMedium =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.HeadlineMediumWeight,
            fontSize = VglsTypeScaleTokens.HeadlineMediumSize,
            lineHeight = VglsTypeScaleTokens.HeadlineMediumLineHeight,
            letterSpacing = VglsTypeScaleTokens.HeadlineMediumTracking,
        )
    val HeadlineSmall =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.HeadlineSmallWeight,
            fontSize = VglsTypeScaleTokens.HeadlineSmallSize,
            lineHeight = VglsTypeScaleTokens.HeadlineSmallLineHeight,
            letterSpacing = VglsTypeScaleTokens.HeadlineSmallTracking,
        )
    val LabelLarge =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.LabelLargeWeight,
            fontSize = VglsTypeScaleTokens.LabelLargeSize,
            lineHeight = VglsTypeScaleTokens.LabelLargeLineHeight,
            letterSpacing = VglsTypeScaleTokens.LabelLargeTracking,
        )
    val LabelMedium =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.LabelMediumWeight,
            fontSize = VglsTypeScaleTokens.LabelMediumSize,
            lineHeight = VglsTypeScaleTokens.LabelMediumLineHeight,
            letterSpacing = VglsTypeScaleTokens.LabelMediumTracking,
        )
    val LabelSmall =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.LabelSmallWeight,
            fontSize = VglsTypeScaleTokens.LabelSmallSize,
            lineHeight = VglsTypeScaleTokens.LabelSmallLineHeight,
            letterSpacing = VglsTypeScaleTokens.LabelSmallTracking,
        )
    val TitleLarge =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.TitleLargeWeight,
            fontSize = VglsTypeScaleTokens.TitleLargeSize,
            lineHeight = VglsTypeScaleTokens.TitleSmallLineHeight,
            letterSpacing = VglsTypeScaleTokens.TitleLargeTracking,
        )
    val TitleMedium =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.TitleMediumWeight,
            fontSize = VglsTypeScaleTokens.TitleMediumSize,
            lineHeight = VglsTypeScaleTokens.TitleMediumLineHeight,
            letterSpacing = VglsTypeScaleTokens.TitleMediumTracking,
        )
    val TitleSmall =
        DefaultTextStyle.copy(
            fontWeight = VglsTypeScaleTokens.TitleSmallWeight,
            fontSize = VglsTypeScaleTokens.TitleSmallSize,
            lineHeight = VglsTypeScaleTokens.TitleSmallLineHeight,
            letterSpacing = VglsTypeScaleTokens.TitleSmallTracking,
        )
}

// Android applies PlatformTextStyle(includeFontPadding = false); JVM/desktop has no such knob.
internal expect fun vglsDefaultTextStyle(): TextStyle

internal val DefaultTextStyle = vglsDefaultTextStyle()
