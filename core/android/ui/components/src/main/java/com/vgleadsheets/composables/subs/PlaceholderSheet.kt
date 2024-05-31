package com.vgleadsheets.composables.subs

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.vgleadsheets.bitmaps.SheetGenerator
import com.vgleadsheets.composables.previews.FullscreenBlack
import com.vgleadsheets.images.PagePreview
import com.vgleadsheets.logging.BasicHatchet
import kotlinx.collections.immutable.toImmutableList

@Composable
@Suppress("UNUSED_PARAMETER")
fun PlaceholderSheet(
    pagePreview: PagePreview,
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
            model = pagePreview,
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = modifier
                .alpha(animatedAlphaValue)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun Page(
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        contentAlignment = Center,
        content = content,
        modifier = modifier
            .aspectRatio(0.77272f)
            .background(Color.White)
    )
}

private fun createImageBitmap(
    context: Context,
    pagePreview: PagePreview,
): ImageBitmap {
    println("Generating...")

    val hatchet = BasicHatchet()
    val generator = SheetGenerator(
        context,
        hatchet,
        "https://jetpackcompose.com"
    )

    val bitmap = generator.generateLoadingSheet(
        1000,
        pagePreview.title,
        pagePreview.transposition,
        pagePreview.gameName,
        pagePreview.composers
    )

    return bitmap.asImageBitmap()
}

@Preview
@Composable
private fun PortraitLoadingKirby() {
    FullscreenBlack {
        SampleLoadingKirby()
    }
}

@Preview(widthDp = 600, heightDp = 400)
@Composable
private fun LandscapeLoadingKirby() {
    FullscreenBlack {
        SampleLoadingKirby()
    }
}

@Preview
@Composable
private fun PortraitLoadingArms() {
    FullscreenBlack {
        SampleLoadingArms()
    }
}

@Composable
private fun SampleLoadingKirby() {
    val pagePreview = PagePreview(
        "A Trip to Alivel Mall",
        "C",
        "Kirby and the Forgotten Land",
        listOf(
            "Hirokazu Ando",
        ).toImmutableList()
    )
    PlaceholderSheet(
        pagePreview = pagePreview,
        seed = 1234L,
        modifier = Modifier,
    )
}

@Composable
private fun SampleLoadingArms() {
    val pagePreview = PagePreview(
        "Grand Prix (Title)",
        "Vocals",
        "Arms",
        listOf(
            "Atsuko Asahi",
            "Yasuaki Iwata"
        ).toImmutableList()
    )
    PlaceholderSheet(
        pagePreview = pagePreview,
        seed = 1234L,
        modifier = Modifier,
    )
}
