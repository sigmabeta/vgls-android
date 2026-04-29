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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.VglsAction
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.previews.PreviewSheet
import com.vgleadsheets.images.LoadingIndicatorConfig
import com.vgleadsheets.images.PdfSize
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.pdf.ZOOM_MAX_PDF
import com.vgleadsheets.pdf.subsample.LocalPdfSubsampler
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.toImmutableList
import me.saket.telephoto.subsamplingimage.SubSamplingImage
import me.saket.telephoto.subsamplingimage.rememberSubSamplingImageState
import me.saket.telephoto.zoomable.ZoomSpec
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.zoomable

@Composable
@Suppress("LongMethod", "ReturnCount")
fun ZoomableSheet(
    pdfConfigById: PdfConfigById,
    contentDescription: String?,
    loadingIndicatorConfig: LoadingIndicatorConfig,
    zoomSpec: ZoomSpec,
    actionSink: ActionSink,
    modifier: Modifier,
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
            zoomSpec = zoomSpec,
            actionSink = actionSink,
            modifier = modifier,
        )
    }
}

@Composable
private fun Content(
    pdfConfigById: PdfConfigById,
    contentDescription: String?,
    loadingIndicatorConfig: LoadingIndicatorConfig,
    zoomSpec: ZoomSpec,
    actionSink: ActionSink,
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

    val zoomableState = rememberZoomableState(
        zoomSpec,
    )
    zoomableState.contentScale = ContentScale.Fit

    val imageSourceFactory = LocalPdfSubsampler.current
    val imageSource = remember { imageSourceFactory.create(data = pdfConfigByIdWithSize) }

    val imageState = rememberSubSamplingImageState(
        zoomableState = zoomableState,
        imageSource = imageSource,
    )

    val zoomableModifier = modifier
        .fillMaxSize()
        .zoomable(
            state = zoomableState,
            onClick = { actionSink.sendAction(VglsAction.PageClicked) },
        )

    val isZoomedIn: Boolean by remember {
        derivedStateOf {
            zoomableState.contentTransformation.scaleMetadata.userZoom > 1f
        }
    }

    LaunchedEffect(isZoomedIn) {
        if (isZoomedIn) {
            actionSink.sendAction(VglsAction.PageZoomedIn)
        } else {
            actionSink.sendAction(VglsAction.PageZoomedOutMax)
        }
    }

    SubSamplingImage(
        state = imageState,
        contentDescription = contentDescription,
        modifier = zoomableModifier
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
        zoomSpec = ZoomSpec(maxZoomFactor = ZOOM_MAX_PDF.toFloat()),
        actionSink = PreviewActionSink { },
        modifier = Modifier.fillMaxSize(),
    )
}

private fun samplePdfConfig() = PdfConfigById(
    0,
    0,
    false,
    samplePdfSize(),
)

private fun samplePdfSize(): PdfSize = PdfSize.MEDIUM
