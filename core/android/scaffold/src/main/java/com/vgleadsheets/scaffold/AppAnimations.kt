package com.vgleadsheets.scaffold

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset

fun enterTransition(): EnterTransition {
    return slideInHorizontally(enterOffsetSpec, rightOffset) + fadeIn(enterFadeSpec)
}

fun popEnterTransition(): EnterTransition {
    return slideInHorizontally(enterOffsetSpec, leftOffset) + fadeIn(enterFadeSpec)
}

fun exitTransition(): ExitTransition {
    return slideOutHorizontally(exitOffsetSpec, leftOffset) + fadeOut(exitFadeSpec)
}

fun popExitTransition(): ExitTransition {
    return slideOutHorizontally(exitOffsetSpec, rightOffset) + fadeOut(exitFadeSpec)
}

private const val DURATION_ANIMATION_ENTER = 500
private const val DURATION_ANIMATION_EXIT = 300

private const val OFFSET_DIVISOR_RIGHT = 2
private const val OFFSET_DIVISOR_LEFT = -3

private val enterOffsetSpec = tween<IntOffset>(DURATION_ANIMATION_ENTER, easing = EaseOutCubic)
private val exitOffsetSpec = tween<IntOffset>(DURATION_ANIMATION_EXIT, easing = EaseInCubic)

private val enterFadeSpec: FiniteAnimationSpec<Float> = tween(DURATION_ANIMATION_ENTER)
private val exitFadeSpec: FiniteAnimationSpec<Float> = tween(DURATION_ANIMATION_EXIT)

private val rightOffset = { fullWidth: Int -> fullWidth / OFFSET_DIVISOR_RIGHT }
private val leftOffset = { fullWidth: Int -> fullWidth / OFFSET_DIVISOR_LEFT }
