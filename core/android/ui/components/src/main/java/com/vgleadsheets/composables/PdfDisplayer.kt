package com.vgleadsheets.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.composables.previews.FullScreenOf
import com.vgleadsheets.pdf.PdfToBitmapRenderer
import com.vgleadsheets.perf.LocalLogger
import java.io.File
import kotlin.random.Random

@Composable
fun PdfDisplayer(
    pdfPath: String,
    zoom: Float,
    modifier: Modifier = Modifier
) {
    val logger = LocalLogger.current
    val bitmapRenderer = remember { PdfToBitmapRenderer(logger) }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        val (maxWidthPx, maxHeightPx) = with(LocalDensity.current) {
            this@BoxWithConstraints.maxWidth.toPx().toInt() to this@BoxWithConstraints.maxHeight.toPx().toInt()
        }

        val bitmap = bitmapRenderer.renderToBitmap(
            File(pdfPath),
            0,
            width = maxWidthPx,
            height = maxHeightPx,
            zoom = zoom
        ).asImageBitmap()

        Image(
            painter = BitmapPainter(
                image = bitmap,
                filterQuality = FilterQuality.None,
            ),
            contentDescription = null,
            contentScale = ContentScale.None,
            modifier = modifier
                .align(Alignment.Center)
                .background(Color.White)
                .wrapContentSize(),
        )
    }
}

@Preview
@Composable
private fun Light() {
    val randomizer = Random(RANDOMIZER_SEED)
    FullScreenOf { paddingValues ->
        Sample(randomizer.nextLong(), paddingValues)
    }
}

@Preview
@Composable
private fun Dark() {
    val randomizer = Random(RANDOMIZER_SEED)
    FullScreenOf(darkTheme = true) { paddingValues ->
        Sample(randomizer.nextLong(), paddingValues)
    }
}

@Composable
private fun Sample(
    seed: Long,
    paddingValues: PaddingValues,
) {
    PdfDisplayer(
        pdfPath = "",
        modifier = Modifier.padding(paddingValues),
        zoom = 1f
    )
}

private const val RANDOMIZER_SEED = 12301L
