package com.vgleadsheets.composables.subs

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.images.PagePreview
import com.vgleadsheets.themes.VglsMaterial

@Composable
@Suppress("UNUSED_PARAMETER")
fun PlaceholderSheet(
    pagePreview: PagePreview,
    seed: Long,
    modifier: Modifier,
    eventListener: SheetPageListModel.ImageListener
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

    // Container
    Box(
        contentAlignment = Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Page
        Box(
            contentAlignment = Center,
            modifier = modifier
                .aspectRatio(0.77272f)
                .background(Color.White)
        ) {
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
}

@Preview
@Composable
private fun PortraitLoadingKirby() {
    VglsMaterial {
        Box(
            contentAlignment = Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            val painter = rememberAsyncImagePainter(
                model = with(ImageRequest.Builder(LocalContext.current)) {
                    data("")
                    build()
                }
            )

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            )
            SampleLoadingKirby()
        }
    }
}

@Preview(widthDp = 600, heightDp = 400)
@Composable
private fun LandscapeLoadingKirby() {
    VglsMaterial {
        Box(
            contentAlignment = Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            val painter = rememberAsyncImagePainter(
                model = with(ImageRequest.Builder(LocalContext.current)) {
                    data("")
                    build()
                }
            )

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            )
            SampleLoadingKirby()
        }
    }
}

@Preview
@Composable
private fun PortraitLoadingArms() {
    VglsMaterial {
        Box(
            contentAlignment = Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            val painter = rememberAsyncImagePainter(
                model = with(ImageRequest.Builder(LocalContext.current)) {
                    data("")
                    build()
                }
            )

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            )
            SampleLoadingArms()
        }
    }
}

@Composable
private fun SampleLoadingKirby() {
    PlaceholderSheet(
        pagePreview = PagePreview(
            "A Trip to Alivel Mall",
            "C",
            "Kirby and the Forgotten Land",
            listOf(
                "Hirokazu Ando",
            )
        ),
        seed = 1234L,
        modifier = Modifier,
        eventListener = NOOP_LISTENER,
    )
}

@Composable
private fun SampleLoadingArms() {
    PlaceholderSheet(
        pagePreview = PagePreview(
            "Grand Prix (Title)",
            "Vocals",
            "Arms",
            listOf(
                "Atsuko Asahi",
                "Yasuaki Iwata"
            )
        ),
        seed = 1234L,
        modifier = Modifier,
        eventListener = NOOP_LISTENER,
    )
}
