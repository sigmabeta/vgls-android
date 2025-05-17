package com.vgleadsheets.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.bitmaps.SheetConstants
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.subs.CrossfadeSheet
import com.vgleadsheets.images.LoadingIndicatorConfig
import com.vgleadsheets.images.PdfSize
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.perf.BuildConfig
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlinx.collections.immutable.toImmutableList

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
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .maybeClickable(pageNumber, actionSink)
            .maybeBackground(actuallyZoomable)

    ) {
        var scale by remember { mutableFloatStateOf(1f) }

        val actualModifier = Modifier
            .wrapContentSize()
            .maybeZoomable(
                actuallyZoomable,
                actionSink,
                portrait,
                this,
                onScaleUpdate = { newScale -> scale = newScale }
            )

        CrossfadeSheet(
            pdfConfigById = pdfConfigById,
            contentDescription = contentDescription,
            loadingIndicatorConfig = loadingIndicatorConfig,
            sheetId = sheetId,
            showDebug = showDebug,
            modifier = actualModifier,
            simulateError = simulateError
        )
    }
}

@Composable
private fun Modifier.maybeZoomable(
    actuallyZoomable: Boolean,
    actionSink: ActionSink,
    portrait: Boolean,
    boxWithConstraintsScope: BoxWithConstraintsScope,
    onScaleUpdate: (Float) -> Unit,
) = if (actuallyZoomable) {
    this.zoomableModifier(portrait, actionSink, boxWithConstraintsScope, onScaleUpdate)
} else {
    this
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

@Composable
private fun Modifier.zoomableModifier(
    portrait: Boolean,
    actionSink: ActionSink,
    boxWithConstraintsScope: BoxWithConstraintsScope,
    onScaleUpdate: (Float) -> Unit,
): Modifier {
    var zoomed by remember { mutableStateOf(false) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var zoomCommand by remember { mutableStateOf(AnimationCommand.ZOOM_OUT) }
    var doubleTapOffset by remember { mutableStateOf(Offset.Zero) }

    zoomed = scale != ZOOM_NONE

    val animScale = getAnimatedScale(zoomCommand, scale)
    val animOffset = getAnimatedOffset(zoomCommand, doubleTapOffset, offset) {
        zoomCommand = AnimationCommand.NONE
    }

    if (zoomCommand != AnimationCommand.NONE) {
        scale = animScale
        offset = animOffset
        onScaleUpdate(animScale)
    }

    return pointerInput(Unit) {
        detectTransformGestures(
            onGesture = { centroid, panChange, zoomChange, _ ->
                zoomCommand = AnimationCommand.NONE
                val coercedScale = (scale * zoomChange).coerceIn(ZOOM_NONE, ZOOM_MAX)

                scale = coercedScale
                onScaleUpdate(coercedScale)

                offset = if (zoomChange in 0.995f..1.005f) {
                    boxWithConstraintsScope.calculateOffsetFromPrevious(
                        offset,
                        panChange,
                        coercedScale,
                        portrait,
                    )
                } else {
                    val gestureCenterAsOffset = -boxWithConstraintsScope.calculateOffsetFromTopLeftNoCoerce(centroid)

                    val zoomComponent = if (zoomChange > 1f) {
                        8
                    } else {
                        -32
                    }

                    val scaleFactor = zoomComponent.toFloat() //* coercedScale
                    val moveBy = (gestureCenterAsOffset / scaleFactor).requireMinimum(1.0f)
                    val sum = (moveBy - offset)

                    val result = boxWithConstraintsScope.coerceOffset(sum, coercedScale, portrait)
                    result
                }
            }
        )
    }
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = { tapCoordinatesFromTopLeft ->
                    if (!zoomed) {
                        zoomCommand = AnimationCommand.ZOOM_IN
                        doubleTapOffset = boxWithConstraintsScope.calculateOffsetFromTopLeft(
                            tapCoordinatesFromTopLeft,
                            ZOOM_DOUBLETAP,
                            portrait,
                        )
                    } else {
                        zoomCommand = AnimationCommand.ZOOM_OUT
                    }
                },
            )
        }
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y
        )
}

@Composable
private fun getAnimatedScale(animationCommand: AnimationCommand, previous: Float) = animateFloatAsState(
    when (animationCommand) {
        AnimationCommand.ZOOM_IN -> ZOOM_DOUBLETAP
        AnimationCommand.ZOOM_OUT -> ZOOM_NONE
        else -> previous
    }
).value

@Composable
private fun getAnimatedOffset(
    animationCommand: AnimationCommand,
    doubleTapOffset: Offset,
    previousOffset: Offset,
    onAnimationFinish: (Offset) -> Unit
) = animateOffsetAsState(
    targetValue = when (animationCommand) {
        AnimationCommand.ZOOM_IN -> doubleTapOffset
        AnimationCommand.ZOOM_OUT -> Offset.Zero
        else -> previousOffset
    },
    finishedListener = onAnimationFinish
).value

private fun BoxWithConstraintsScope.calculateOffsetFromTopLeft(
    coordsFromTopLeft: Offset,
    scale: Float,
    portrait: Boolean,
): Offset {
    val maxOffset = calculateMaxOffset(scale, portrait)
    val topLeft = Offset(
        constraints.maxWidth / -2f,
        constraints.maxHeight / -2f,
    )

    val result = Offset(
        x = -(topLeft.x + coordsFromTopLeft.x).coerceIn(-maxOffset.x, maxOffset.x),
        y = -(topLeft.y + coordsFromTopLeft.y).coerceIn(-maxOffset.y, maxOffset.y),
    )

    return result
}

private fun BoxWithConstraintsScope.calculateOffsetFromTopLeftNoCoerce(
    coordsFromTopLeft: Offset,
): Offset {
    val topLeft = Offset(
        constraints.maxWidth / -2f,
        constraints.maxHeight / -2f,
    )

    val result = Offset(
        x = -(topLeft.x + coordsFromTopLeft.x),
        y = -(topLeft.y + coordsFromTopLeft.y),
    )

    return result
}

private fun BoxWithConstraintsScope.coerceOffset(
    offset: Offset,
    scale: Float,
    portrait: Boolean,
): Offset {
    val maxOffset = calculateMaxOffset(scale, portrait)

    val result = Offset(
        x = -(offset.x).coerceIn(-maxOffset.x, maxOffset.x),
        y = -(offset.y).coerceIn(-maxOffset.y, maxOffset.y),
    )

    return result
}

private fun BoxWithConstraintsScope.calculateOffsetFromPrevious(
    prevOffset: Offset,
    offsetChange: Offset,
    scale: Float,
    portrait: Boolean,
): Offset {
    val maxOffset = calculateMaxOffset(scale, portrait)

    return Offset(
        x = (prevOffset.x + offsetChange.x).coerceIn(-maxOffset.x, maxOffset.x),
        y = (prevOffset.y + offsetChange.y).coerceIn(-maxOffset.y, maxOffset.y)
    )
}

private fun BoxWithConstraintsScope.calculateMaxOffset(
    scale: Float,
    portrait: Boolean,
): Offset {
    val (width, height) = calculateScaledSheetWidthAndHeight(scale, portrait)

    val constraints = constraints

    val maxOffsetX = (width - constraints.maxWidth).coerceAtLeast(0f) / 2
    val maxOffsetY = (height - constraints.maxHeight).coerceAtLeast(0f) / 2

    return Offset(maxOffsetX, maxOffsetY)
}

private fun BoxWithConstraintsScope.calculateNormalizedSheetCoord(
    composableOffset: Offset,
    scale: Float,
    portrait: Boolean,
): NormalizedSheetCoordinate {
    val normalizedOffset = normalizeOffset(
        offset = composableOffset,
        scale = scale,
        portrait = portrait,
    )
    return NormalizedSheetCoordinate(
        center = normalizedOffset,
        visibleBoundaries = normalizeBoundaries(
            normalizedOffset = normalizedOffset,
            scale = scale,
            portrait = portrait
        ),
    )
}

private fun BoxWithConstraintsScope.normalizeBoundaries(
    normalizedOffset: Offset,
    scale: Float,
    portrait: Boolean,
): Rect {
    val (sheetWidth, sheetHeight) = calculateSheetWidthAndHeight(portrait)

    val halfSheetWidth = sheetWidth / 2
    val halfSheetHeight = sheetHeight / 2

    val screenWidth = constraints.maxWidth
    val screenHeight = constraints.maxHeight

    val halfNormalizedScaledScreenWidth = ((screenWidth / 4) / scale) / halfSheetWidth
    val halfNormalizedScaledScreenHeight = ((screenHeight / 4) / scale) / halfSheetHeight

    return Rect(
        left = (normalizedOffset.x - halfNormalizedScaledScreenWidth),
        right = (normalizedOffset.x + halfNormalizedScaledScreenWidth),
        top = (normalizedOffset.y - halfNormalizedScaledScreenHeight),
        bottom = (normalizedOffset.y + halfNormalizedScaledScreenHeight),
    )
}

private fun BoxWithConstraintsScope.normalizeOffset(
    offset: Offset,
    scale: Float,
    portrait: Boolean,
): Offset {
    val scaledOffset = -offset / scale

    val (sheetWidth, sheetHeight) = calculateSheetWidthAndHeight(portrait)

    val halfSheetWidth = sheetWidth / 2
    val halfSheetHeight = sheetHeight / 2

    val normalizedXFromCenter = scaledOffset.x / halfSheetWidth
    val normalizedYFromCenter = scaledOffset.y / halfSheetHeight

    val normalizedXFromTopLeft = (normalizedXFromCenter + 1.0f) / 2.0f
    val normalizedYFromTopLeft = (normalizedYFromCenter + 1.0f) / 2.0f

    return Offset(
        normalizedXFromTopLeft,
        normalizedYFromTopLeft,
    )
}

private fun BoxWithConstraintsScope.calculateScaledSheetWidthAndHeight(
    scale: Float,
    portrait: Boolean,
) = if (portrait) {
    val width = constraints.maxWidth.toFloat()
    val height = width / SheetConstants.ASPECT_RATIO

    scale * width to scale * height
} else {
    val height = constraints.maxHeight.toFloat()
    val width = height * SheetConstants.ASPECT_RATIO

    scale * width to scale * height
}

private fun BoxWithConstraintsScope.calculateSheetWidthAndHeight(
    portrait: Boolean,
) = if (portrait) {
    val width = constraints.maxWidth.toFloat()
    val height = width / SheetConstants.ASPECT_RATIO

    width to height
} else {
    val height = constraints.maxHeight.toFloat()
    val width = height * SheetConstants.ASPECT_RATIO

    width to height
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
