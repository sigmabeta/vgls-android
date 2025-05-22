package com.vgleadsheets.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.previews.PreviewSheet
import com.vgleadsheets.images.LoadingIndicatorConfig
import com.vgleadsheets.images.PdfSize
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.pdf.subsample.LocalPdfSubsampler
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.toImmutableList
import me.saket.telephoto.subsamplingimage.SubSamplingImage
import me.saket.telephoto.subsamplingimage.rememberSubSamplingImageState
import me.saket.telephoto.zoomable.ZoomableState
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.zoomable

@Composable
@Suppress("LongMethod", "ReturnCount")
fun ZoomableSheet(
    pdfConfigById: PdfConfigById,
    contentDescription: String?,
    loadingIndicatorConfig: LoadingIndicatorConfig,
    actionSink: ActionSink,
    modifier: Modifier,
    zoomableState: ZoomableState,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Content(
            pdfConfigById = pdfConfigById,
            contentDescription = contentDescription,
            loadingIndicatorConfig = loadingIndicatorConfig,
            actionSink = actionSink,
            modifier = modifier,
            zoomableState = zoomableState,
        )
    }
}

@Composable
private fun Content(
    pdfConfigById: PdfConfigById,
    contentDescription: String?,
    loadingIndicatorConfig: LoadingIndicatorConfig,
    actionSink: ActionSink,
    zoomableState: ZoomableState,
    modifier: Modifier,
) {
    val windowInfo = LocalWindowInfo.current
    val containerSize = windowInfo.containerSize

    val pdfConfigByIdWithSize = pdfConfigById.copy(
        maxWidth = containerSize.width,
        maxHeight = containerSize.height,
    )

    val loadingIndicatorConfigWithSize = loadingIndicatorConfig.copy(
        maxWidth = containerSize.width,
        maxHeight = containerSize.height,
    )

    if (LocalInspectionMode.current) {
        PreviewSheet(
            loadingIndicatorConfigWithSize,
            modifier
        )
        return
    }

    val imageSourceFactory = LocalPdfSubsampler.current
    val imageSource = remember { imageSourceFactory.create(data = pdfConfigByIdWithSize) }

    val imageState = rememberSubSamplingImageState(
        zoomableState = zoomableState,
        imageSource = imageSource,
    )

    val isZoomed by remember { derivedStateOf { (zoomableState.zoomFraction ?: 0f) > 0f } }

    LaunchedEffect(isZoomed) {
        if (isZoomed) {
            actionSink.sendAction(VglsAction.PageZoomedIn(pdfConfigById.pageNumber))
        } else {
            actionSink.sendAction(VglsAction.PageZoomedOutMax)
        }
    }

    zoomableState.zoomFraction
    SubSamplingImage(
        state = imageState,
        contentDescription = contentDescription,
        modifier = modifier
            .fillMaxSize()
            .zoomable(
                state = zoomableState,
                onClick = { actionSink.sendAction(VglsAction.PageClicked) },
            ),
    )
}

@Preview
@Composable
private fun Portrait() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            SampleSheetPageOne()
        }
    }
}

@Composable
private fun SampleSheetPageOne() {
    ZoomableSheet(
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
        modifier = Modifier.fillMaxSize(),
        actionSink = PreviewActionSink { },
        zoomableState = rememberZoomableState(),
    )
}

private fun samplePdfConfig() = PdfConfigById(
    0,
    0,
    false,
    samplePdfSize(),
)

private fun samplePdfSize(): PdfSize = PdfSize.MEDIUM
