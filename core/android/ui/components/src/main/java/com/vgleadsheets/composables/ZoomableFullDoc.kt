package com.vgleadsheets.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.bitmaps.SheetConstants
import com.vgleadsheets.components.ZoomableSheetPageListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.previews.PreviewSheet
import com.vgleadsheets.composables.utils.ImageSize
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
fun ZoomableFullDocItem(
    model: ZoomableSheetPageListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val contentDescription = "${model.title} from ${model.gameName}, page ${model.pageNumber + 1}"

    val heightDp = ImageSize.LARGE_HEIGHT.size
    val (maxWidthPx, maxHeightPx) = with(LocalDensity.current) {
        heightDp.toPx() * SheetConstants.ASPECT_RATIO to heightDp.toPx()
    }

    ZoomableFullDoc(
        pdfConfigById = model.pdfConfigById,
        contentDescription = contentDescription,
        actionSink = actionSink,
        loadingIndicatorConfig = LoadingIndicatorConfig(
            model.title,
            model.gameName,
            model.composers,
            model.pageNumber,
            model.pdfConfigById.pdfSize,
            maxWidth = maxWidthPx.toInt(),
            maxHeight = maxHeightPx.toInt(),
        ),
        modifier = modifier
            .fillMaxSize()
            .padding(padding),
    )
}

@Composable
@Suppress("LongMethod", "ReturnCount")
private fun ZoomableFullDoc(
    pdfConfigById: PdfConfigById,
    contentDescription: String?,
    loadingIndicatorConfig: LoadingIndicatorConfig,
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
    actionSink: ActionSink,
    modifier: Modifier,
) {
    val windowInfo = LocalWindowInfo.current
    val containerSize = windowInfo.containerSize

    val pdfConfigByIdWithSize = pdfConfigById.copy(
        pageNumber = null,
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

    val zoomSpec = ZoomSpec(maxZoomFactor = ZOOM_MAX_PDF.toFloat())
    val zoomableState = rememberZoomableState(
        zoomSpec,
    )
    zoomableState.contentScale = ContentScale.FillHeight

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
        modifier = Modifier.fillMaxSize(),
        actionSink = PreviewActionSink { },
    )
}

private fun samplePdfConfig() = PdfConfigById(
    0,
    0,
    false,
    samplePdfSize(),
)

private fun samplePdfSize(): PdfSize = PdfSize.MEDIUM
