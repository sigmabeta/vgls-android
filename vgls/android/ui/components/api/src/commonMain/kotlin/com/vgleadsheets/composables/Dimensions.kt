package com.vgleadsheets.composables

import androidx.compose.ui.unit.dp

/**
 * Hoisted layout dimensions, replacing the Android `R.dimen.*` values (dimens.xml) so the shared UI
 * has no `dimensionResource` dependency. First cut: the default (compact) values. The width-class-
 * responsive variants that dimens.xml carried (margin_side grows on w600dp/w800dp;
 * min_clickable_size grows on sw600dp) are a follow-up — key them off LocalDisplayWidthClass.
 */
object Dimensions {
    val marginMedium = 16.dp
    val marginSide = 16.dp
    val minClickableSize = 48.dp
}
