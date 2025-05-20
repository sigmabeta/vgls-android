package com.vgleadsheets.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.bitmaps.SheetConstants
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.previews.PreviewSheet
import com.vgleadsheets.composables.subs.PlaceholderSheet
import com.vgleadsheets.composables.utils.ImageSize
import com.vgleadsheets.images.LoadingIndicatorConfig
import com.vgleadsheets.images.PdfSize
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.pdf.subsample.LocalPdfSubsampler
import com.vgleadsheets.perf.BuildConfig
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.id
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlinx.collections.immutable.toImmutableList
import me.saket.telephoto.subsamplingimage.SubSamplingImage
import me.saket.telephoto.subsamplingimage.rememberSubSamplingImageState
import me.saket.telephoto.zoomable.ZoomSpec
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.zoomable

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("LongMethod", "ReturnCount")
fun ZoomableSheet(
    pdfConfigById: PdfConfigById,
    contentDescription: String?,
    loadingIndicatorConfig: LoadingIndicatorConfig,
    sheetId: Long,
    actuallyZoomable: Boolean,
    showDebug: Boolean,
    actionSink: ActionSink,
    portrait: Boolean,
    modifier: Modifier,
    simulateError: Boolean = false
) {
    val pageNumber = pdfConfigById.pageNumber
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .maybeClickable(pageNumber, actionSink)
            .maybeBackground(actuallyZoomable)

    ) {
        if (pdfConfigById.pdfSize == PdfSize.FILL) {
            BoxWithConstraints {
                Content(
                    pdfConfigById = pdfConfigById,
                    contentDescription = contentDescription,
                    loadingIndicatorConfig = loadingIndicatorConfig,
                    sheetId = sheetId,
                    showDebug = showDebug,
                    scope = this,
                    modifier = modifier,
                    simulateError = simulateError
                )
            }
        } else {
            Box {
                Content(
                    pdfConfigById = pdfConfigById,
                    contentDescription = contentDescription,
                    loadingIndicatorConfig = loadingIndicatorConfig,
                    sheetId = sheetId,
                    showDebug = showDebug,
                    modifier = modifier,
                    simulateError = simulateError,
                    scope = null
                )
            }
        }
    }
}

@Composable
private fun BoxScope.Content(
    pdfConfigById: PdfConfigById,
    contentDescription: String?,
    loadingIndicatorConfig: LoadingIndicatorConfig,
    sheetId: Long,
    showDebug: Boolean,
    scope: BoxWithConstraintsScope?,
    modifier: Modifier,
    simulateError: Boolean
) {
    val pdfConfigByIdWithSize = withSize(
        pdfConfigById,
        scope
    )

    println("$pdfConfigByIdWithSize")

    val loadingIndicatorConfigWithSize = withSize(
        loadingIndicatorConfig,
        scope
    )

    val bgModifier = modifier.bgModifier()

    if (simulateError) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = bgModifier.fillMaxSize()
        ) {
            ErrorState(
                pdfConfigByIdWithSize,
                modifier,
                showDebug,
                loadingIndicatorConfig = loadingIndicatorConfigWithSize,
                sheetId = sheetId,
                IllegalArgumentException("Oops it didn't work."),
            ) { }
        }
        return
    }

    if (LocalInspectionMode.current) {
        PreviewSheet(
            loadingIndicatorConfigWithSize,
            modifier.bgModifier()
        )
        return
    }

    val imageSourceFactory = LocalPdfSubsampler.current
    val imageSource = remember { imageSourceFactory.create(data = pdfConfigById) }

    val zoomableState = rememberZoomableState(
        zoomSpec = ZoomSpec(
            maxZoomFactor = 4f,
        )
    )
    val imageState = rememberSubSamplingImageState(
        zoomableState = zoomableState,
        imageSource = imageSource,
    )

    SubSamplingImage(
        state = imageState,
        contentDescription = contentDescription,
        modifier = bgModifier
            .fillMaxSize()
            .zoomable(zoomableState)
    )
}

@Composable
private fun Modifier.bgModifier(): Modifier {
    val bgColor = if (BuildConfig.DEBUG) {
        Color(1f, 1f, 0.8f, 1f)
    } else {
        Color.White
    }

    return background(bgColor)
}

@Composable
private fun BoxScope.withSize(
    withoutSize: PdfConfigById,
    scope: BoxWithConstraintsScope?
): PdfConfigById {
    with(LocalDensity.current) {
        val scopeWidth = scope?.maxWidth ?: Int.MAX_VALUE.dp
        val scopeHeight = scope?.maxHeight ?: Int.MAX_VALUE.dp
        val (maxWidth, maxHeight) = when (withoutSize.pdfSize) {
            PdfSize.THUMBNAIL -> ImageSize.THUMBNAIL.size to ImageSize.THUMBNAIL.size
            PdfSize.MEDIUM -> scopeWidth to ImageSize.MEDIUM_HEIGHT.size
            PdfSize.LARGE -> scopeWidth to ImageSize.LARGE_HEIGHT.size
            PdfSize.FILL -> scopeWidth to scopeHeight
        }

        val maxWidthInt = maxWidth.toPx().roundToInt()
        val maxHeightInt = maxHeight.toPx().roundToInt()

        return@withSize withoutSize.copy(
            maxWidth = maxWidthInt,
            maxHeight = maxHeightInt,
        )
    }
}

@Composable
private fun BoxScope.withSize(
    withoutSize: LoadingIndicatorConfig,
    scope: BoxWithConstraintsScope?
): LoadingIndicatorConfig {
    with(LocalDensity.current) {
        val scopeWidth = scope?.maxWidth ?: Int.MAX_VALUE.dp
        val scopeHeight = scope?.maxHeight ?: Int.MAX_VALUE.dp
        val (maxWidth, maxHeight) = when (withoutSize.loaderSize) {
            PdfSize.THUMBNAIL -> ImageSize.THUMBNAIL.size to ImageSize.THUMBNAIL.size
            PdfSize.MEDIUM -> scopeWidth to ImageSize.MEDIUM_HEIGHT.size
            PdfSize.LARGE -> scopeWidth to ImageSize.LARGE_HEIGHT.size
            PdfSize.FILL -> scopeWidth to scopeHeight
        }

        val maxWidthInt = maxWidth.toPx().roundToInt()
        val maxHeightInt = maxHeight.toPx().roundToInt()

        return@withSize withoutSize.copy(
            maxWidth = withoutSize.maxWidth ?: maxWidthInt,
            maxHeight = withoutSize.maxHeight ?: maxHeightInt,
        )
    }
}

@Composable
@Suppress("MagicNumber")
private fun BoxScope.ErrorState(
    pdfConfigById: PdfConfigById,
    modifier: Modifier,
    showDebug: Boolean,
    loadingIndicatorConfig: LoadingIndicatorConfig,
    sheetId: Long,
    error: Throwable,
    errorOnClick: () -> Unit,
) {
    PlaceholderSheet(
        loadingIndicatorConfig = loadingIndicatorConfig,
        seed = sheetId,
        modifier = modifier.bgModifier()
    )

    // Transparent, clickable overlay
    Box(
        modifier = Modifier
            .clickable(onClick = errorOnClick)
            .matchParentSize()
            .background(Color(0, 0, 0, 128))
    ) { }

    val height = with(LocalDensity.current) {
        loadingIndicatorConfig.maxHeight?.toDp()
    } ?: 32.dp

    EmptyListIndicator(
        model = ErrorStateListModel(
            failedOperationName = "Load PDF with ID ${pdfConfigById.songId}",
            errorString = stringResource(StringId.ERROR_IMAGE_NETWORK.id()),
            error = error
        ),
        onBlack = true,
        showDebug = showDebug,
        modifier = modifier
            .height(height)
            .aspectRatio(SheetConstants.ASPECT_RATIO)
    )
}

@Composable
private fun Modifier.maybeBackground(actuallyZoomable: Boolean) = if (BuildConfig.DEBUG && actuallyZoomable) {
    background(Color.Blue)
} else {
    this
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.maybeClickable(
    pageNumber: Int?,
    actionSink: ActionSink
) = if (pageNumber != null) {
    combinedClickable(
        onClick = { actionSink.sendAction(VglsAction.PageClicked) },
        onDoubleClick = { actionSink.sendAction(VglsAction.PageDoubleClicked(pageNumber)) }
    )
} else {
    this
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
        sheetId = 1234L,
        actuallyZoomable = true,
        showDebug = true,
        modifier = Modifier.fillMaxSize(),
        portrait = true,
        actionSink = PreviewActionSink { },
    )
}

private const val ZOOM_NONE = 1.0f
private const val ZOOM_DOUBLETAP = 2.0f
private const val ZOOM_MAX = 8.0f

private enum class AnimationCommand {
    NONE,
    ZOOM_IN,
    ZOOM_OUT,
}

private data class NormalizedSheetCoordinate(
    val center: Offset,
    val visibleBoundaries: Rect
)

private fun Offset.toStringDirectional(): String {
    val directionX = if (this.x > 0) {
        "Right"
    } else {
        "Left"
    }
    val directionY = if (this.y < 0) {
        "Top"
    } else {
        "Bottom"
    }

    return "$directionX: $x | $directionY: $y"
}

private fun Offset.toWordsDirectional(): String {
    val directionX = when {
        x > 0 -> "Rght"
        x < 0 -> "Left"
        else -> "None"

    }

    val directionY = when {
        y < 0 -> "Up  "
        y > 0 -> "Down"
        else -> "None"
    }

    val magnitude = sqrt(x.pow(2) + y.pow(2))
    return "$directionX | $directionY: ${magnitude.roundToInt()}"
}

private fun Offset.requireMinimum(minimum: Float): Offset {
    val newX = if (x.absoluteValue < minimum) {
        x.roundToInt().toFloat()
    } else {
        x
    }
    val newY = if (y.absoluteValue < minimum) {
        y.roundToInt().toFloat()
    } else {
        y
    }

    return Offset(newX, newY)
}

private fun samplePdfConfig() = PdfConfigById(
    0,
    0,
    false,
    samplePdfSize(),
)

private fun samplePdfSize(): PdfSize = PdfSize.MEDIUM
