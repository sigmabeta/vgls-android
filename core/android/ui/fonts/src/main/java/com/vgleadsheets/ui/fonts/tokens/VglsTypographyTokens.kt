package com.vgleadsheets.ui.fonts.tokens

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle

internal object VglsTypographyTokens {
    val BodyLarge =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.BodyLargeFont,
            fontWeight = VglsTypeScaleTokens.BodyLargeWeight,
            fontSize = VglsTypeScaleTokens.BodyLargeSize,
            lineHeight = VglsTypeScaleTokens.BodyLargeLineHeight,
            letterSpacing = VglsTypeScaleTokens.BodyLargeTracking,
        )
    val BodyMedium =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.BodyMediumFont,
            fontWeight = VglsTypeScaleTokens.BodyMediumWeight,
            fontSize = VglsTypeScaleTokens.BodyMediumSize,
            lineHeight = VglsTypeScaleTokens.BodyMediumLineHeight,
            letterSpacing = VglsTypeScaleTokens.BodyMediumTracking,
        )
    val BodySmall =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.BodySmallFont,
            fontWeight = VglsTypeScaleTokens.BodySmallWeight,
            fontSize = VglsTypeScaleTokens.BodySmallSize,
            lineHeight = VglsTypeScaleTokens.BodySmallLineHeight,
            letterSpacing = VglsTypeScaleTokens.BodySmallTracking,
        )
    val DisplayLarge =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.DisplayLargeFont,
            fontWeight = VglsTypeScaleTokens.DisplayLargeWeight,
            fontSize = VglsTypeScaleTokens.DisplayLargeSize,
            lineHeight = VglsTypeScaleTokens.DisplayLargeLineHeight,
            letterSpacing = VglsTypeScaleTokens.DisplayLargeTracking,
        )
    val DisplayMedium =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.DisplayMediumFont,
            fontWeight = VglsTypeScaleTokens.DisplayMediumWeight,
            fontSize = VglsTypeScaleTokens.DisplayMediumSize,
            lineHeight = VglsTypeScaleTokens.DisplayMediumLineHeight,
            letterSpacing = VglsTypeScaleTokens.DisplayMediumTracking,
        )
    val DisplaySmall =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.DisplaySmallFont,
            fontWeight = VglsTypeScaleTokens.DisplaySmallWeight,
            fontSize = VglsTypeScaleTokens.DisplaySmallSize,
            lineHeight = VglsTypeScaleTokens.DisplaySmallLineHeight,
            letterSpacing = VglsTypeScaleTokens.DisplaySmallTracking,
        )
    val HeadlineLarge =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.HeadlineLargeFont,
            fontWeight = VglsTypeScaleTokens.HeadlineLargeWeight,
            fontSize = VglsTypeScaleTokens.HeadlineLargeSize,
            lineHeight = VglsTypeScaleTokens.HeadlineLargeLineHeight,
            letterSpacing = VglsTypeScaleTokens.HeadlineLargeTracking,
        )
    val HeadlineMedium =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.HeadlineMediumFont,
            fontWeight = VglsTypeScaleTokens.HeadlineMediumWeight,
            fontSize = VglsTypeScaleTokens.HeadlineMediumSize,
            lineHeight = VglsTypeScaleTokens.HeadlineMediumLineHeight,
            letterSpacing = VglsTypeScaleTokens.HeadlineMediumTracking,
        )
    val HeadlineSmall =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.HeadlineSmallFont,
            fontWeight = VglsTypeScaleTokens.HeadlineSmallWeight,
            fontSize = VglsTypeScaleTokens.HeadlineSmallSize,
            lineHeight = VglsTypeScaleTokens.HeadlineSmallLineHeight,
            letterSpacing = VglsTypeScaleTokens.HeadlineSmallTracking,
        )
    val LabelLarge =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.LabelLargeFont,
            fontWeight = VglsTypeScaleTokens.LabelLargeWeight,
            fontSize = VglsTypeScaleTokens.LabelLargeSize,
            lineHeight = VglsTypeScaleTokens.LabelLargeLineHeight,
            letterSpacing = VglsTypeScaleTokens.LabelLargeTracking,
        )
    val LabelMedium =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.LabelMediumFont,
            fontWeight = VglsTypeScaleTokens.LabelMediumWeight,
            fontSize = VglsTypeScaleTokens.LabelMediumSize,
            lineHeight = VglsTypeScaleTokens.LabelMediumLineHeight,
            letterSpacing = VglsTypeScaleTokens.LabelMediumTracking,
        )
    val LabelSmall =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.LabelSmallFont,
            fontWeight = VglsTypeScaleTokens.LabelSmallWeight,
            fontSize = VglsTypeScaleTokens.LabelSmallSize,
            lineHeight = VglsTypeScaleTokens.LabelSmallLineHeight,
            letterSpacing = VglsTypeScaleTokens.LabelSmallTracking,
        )
    val TitleLarge =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.TitleLargeFont,
            fontWeight = VglsTypeScaleTokens.TitleLargeWeight,
            fontSize = VglsTypeScaleTokens.TitleLargeSize,
            lineHeight = VglsTypeScaleTokens.TitleSmallLineHeight,
            letterSpacing = VglsTypeScaleTokens.TitleLargeTracking,
        )
    val TitleMedium =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.TitleMediumFont,
            fontWeight = VglsTypeScaleTokens.TitleMediumWeight,
            fontSize = VglsTypeScaleTokens.TitleMediumSize,
            lineHeight = VglsTypeScaleTokens.TitleMediumLineHeight,
            letterSpacing = VglsTypeScaleTokens.TitleMediumTracking,
        )
    val TitleSmall =
        DefaultTextStyle.copy(
            fontFamily = VglsTypeScaleTokens.TitleSmallFont,
            fontWeight = VglsTypeScaleTokens.TitleSmallWeight,
            fontSize = VglsTypeScaleTokens.TitleSmallSize,
            lineHeight = VglsTypeScaleTokens.TitleSmallLineHeight,
            letterSpacing = VglsTypeScaleTokens.TitleSmallTracking,
        )
}

private val DefaultPlatformTextStyle = PlatformTextStyle(
    includeFontPadding = false
)

internal val DefaultTextStyle = TextStyle.Default.copy(platformStyle = DefaultPlatformTextStyle)
