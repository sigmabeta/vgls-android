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
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.vgleadsheets.composables.previews.FullscreenBlack
import com.vgleadsheets.images.LoadingIndicatorConfig
import net.sigmabeta.sage.images.PdfSize
import kotlinx.collections.immutable.toImmutableList

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
    val loadingIndicatorConfig = LoadingIndicatorConfig(
        "A Trip to Alivel Mall",
        "Kirby and the Forgotten Land",
        listOf(
            "Hirokazu Ando",
        ).toImmutableList(),
        loaderSize = PdfSize.MEDIUM,
        pageNumber = 0
    )
    PlaceholderSheet(
        loadingIndicatorConfig = loadingIndicatorConfig,
        seed = 1234L,
        modifier = Modifier,
    )
}

@Composable
private fun SampleLoadingArms() {
    val loadingIndicatorConfig = LoadingIndicatorConfig(
        "Grand Prix (Title)",
        "Arms",
        listOf(
            "Atsuko Asahi",
            "Yasuaki Iwata"
        ).toImmutableList(),
        loaderSize = PdfSize.MEDIUM,
        pageNumber = 0
    )
    PlaceholderSheet(
        loadingIndicatorConfig = loadingIndicatorConfig,
        seed = 1234L,
        modifier = Modifier,
    )
}
