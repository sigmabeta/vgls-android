package com.vgleadsheets.composables.subs

import android.content.Context
import android.util.Log.VERBOSE
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.util.Logger
import com.vgleadsheets.bitmaps.SheetGenerator
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.composables.previews.FullscreenBlack
import com.vgleadsheets.images.PagePreview
import com.vgleadsheets.images.SheetPreviewFetcher
import com.vgleadsheets.logging.BasicHatchet

@Composable
@Suppress("UNUSED_PARAMETER")
fun PlaceholderSheet(
    pagePreview: PagePreview,
    seed: Long,
    isInPreviewView: Boolean = false,
    eventListener: SheetPageListModel.ImageListener,
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
        if (isInPreviewView) {
            val loader = createImageLoader(LocalContext.current)
            AsyncImage(
                imageLoader = loader,
                model = pagePreview,
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = modifier
                    .alpha(animatedAlphaValue)
                    .fillMaxWidth(),
            )
        } else {
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

private fun createImageLoader(context: Context): ImageLoader {
    val hatchet = BasicHatchet()
    val generator = SheetGenerator(
        context,
        hatchet,
        "https://jetpackcompose.com"
    )

    val sheetPreviewFetcherFactory = SheetPreviewFetcher.Factory(generator)

    val coilLogger = object : Logger {
        override var level = VERBOSE

        override fun log(
            tag: String,
            priority: Int,
            message: String?,
            throwable: Throwable?
        ) {
            if (throwable != null) {
                hatchet.e(tag, "Message: $message Error: $throwable")
            } else {
                hatchet.i(tag, message ?: "Blank message")
            }
        }
    }

    return ImageLoader.Builder(context)
        .networkObserverEnabled(false)
        .components {
            add(sheetPreviewFetcherFactory)
        }
        .logger(coilLogger)
        .build()
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
        isInPreviewView = true,
        eventListener = NOOP_LISTENER,
        modifier = Modifier,
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
        isInPreviewView = true,
        eventListener = NOOP_LISTENER,
        modifier = Modifier,
    )
}
