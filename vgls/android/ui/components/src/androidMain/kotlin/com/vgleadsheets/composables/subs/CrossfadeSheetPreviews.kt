package com.vgleadsheets.composables.subs

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.LocalPlatformContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.vgleadsheets.bitmaps.SheetConstants
import net.sigmabeta.sage.components.ErrorStateListModel
import com.vgleadsheets.composables.EmptyListIndicator
import com.vgleadsheets.composables.previews.PreviewSheet
import com.vgleadsheets.composables.utils.ImageSize
import com.vgleadsheets.images.LoadingIndicatorConfig
import net.sigmabeta.sage.images.PdfSize
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.ui.perf.isPerfMeasurementEnabled
import com.vgleadsheets.strings.text
import com.vgleadsheets.strings.imageLoadErrorStringId
import com.vgleadsheets.ui.theme.AppTheme
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.roundToInt

@Preview
@Composable
private fun PortraitTitleSheet() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            SampleSheetPageOne()
        }
    }
}

@Preview
@Composable
private fun PortraitOtherSheet() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            SampleSheetPageTwo()
        }
    }
}

@Preview
@Composable
private fun PortraitLoading() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            SampleLoading()
        }
    }
}

@Preview
@Composable
private fun PortraitError() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            SampleError()
        }
    }
}

@Composable
private fun SampleLoading() {
    CrossfadeSheet(
        pdfConfigById = samplePdfConfig(),
        contentDescription = null,
        loadingIndicatorConfig = LoadingIndicatorConfig(
            title = "A Trip to Alivel Mall",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 0,
            loaderSize = samplePdfSize(),
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        showDebug = true,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun SampleSheetPageOne() {
    CrossfadeSheet(
        pdfConfigById = samplePdfConfig(),
        contentDescription = null,
        loadingIndicatorConfig = LoadingIndicatorConfig(
            title = "A Trip to Alivel Mall",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 0,
            loaderSize = samplePdfSize(),
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        showDebug = true,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun SampleSheetPageTwo() {
    CrossfadeSheet(
        pdfConfigById = samplePdfConfig(),
        contentDescription = null,
        loadingIndicatorConfig = LoadingIndicatorConfig(
            title = "A Trip to Alivel Mall",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 1,
            loaderSize = samplePdfSize(),
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        showDebug = true,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun SampleError() {
    CrossfadeSheet(
        pdfConfigById = samplePdfConfig(),
        contentDescription = null,
        loadingIndicatorConfig = LoadingIndicatorConfig(
            title = "A Trip to Alivel Mall",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 0,
            loaderSize = samplePdfSize(),
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        showDebug = true,
        modifier = Modifier.fillMaxWidth(),
        simulateError = true,
    )
}

private fun samplePdfConfig() = PdfConfigById(
    0,
    0,
    false,
    samplePdfSize(),
)

private fun samplePdfSize(): PdfSize = PdfSize.MEDIUM
