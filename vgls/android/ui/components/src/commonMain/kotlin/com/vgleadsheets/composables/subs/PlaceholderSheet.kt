package com.vgleadsheets.composables.subs

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.vgleadsheets.composables.previews.FullscreenBlack
import com.vgleadsheets.images.LoadingIndicatorConfig
import net.sigmabeta.sage.images.PdfSize
import kotlinx.collections.immutable.toImmutableList

@Composable
@Suppress("UNUSED_PARAMETER")
fun PlaceholderSheet(
    loadingIndicatorConfig: LoadingIndicatorConfig,
    seed: Long,
    modifier: Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedAlphaValue by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Page(modifier) {
        AsyncImage(
            model = loadingIndicatorConfig,
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = modifier
                .wrapContentSize()
                .alpha(animatedAlphaValue),
        )
    }
}

@Composable
private fun Page(
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        content = content,
        modifier = modifier
    )
}
