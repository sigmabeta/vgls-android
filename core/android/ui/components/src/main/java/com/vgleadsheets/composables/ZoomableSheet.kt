package com.vgleadsheets.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.previews.SheetConstants
import com.vgleadsheets.composables.subs.CrossfadeSheet
import com.vgleadsheets.images.LoadingIndicatorConfig
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.perf.BuildConfig
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("LongMethod", "ReturnCount")
fun ZoomableSheet(
    sourceInfo: SourceInfo,
    contentDescription: String?,
    loadingIndicatorConfig: LoadingIndicatorConfig,
    sheetId: Long,
    fillMaxWidth: Boolean,
    actuallyZoomable: Boolean,
    showDebug: Boolean,
    actionSink: ActionSink,
    portrait: Boolean,
    modifier: Modifier,
    simulateError: Boolean = false
) {
    val pageNumber = (sourceInfo.info as? PdfConfigById)?.pageNumber
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .maybeClickable(pageNumber, actionSink)
            .maybeBackground(actuallyZoomable)

    ) {
        val actualModifier = Modifier
            .fillMaxSize()
            .maybeZoomable(actuallyZoomable, portrait, this)

        CrossfadeSheet(
            sourceInfo = sourceInfo,
            contentDescription = contentDescription,
            loadingIndicatorConfig = loadingIndicatorConfig,
            sheetId = sheetId,
            portrait = portrait,
            fillMaxWidth = fillMaxWidth,
            showDebug = showDebug,
            modifier = actualModifier,
            simulateError = simulateError
        )
    }
}

@Composable
private fun Modifier.maybeZoomable(
    actuallyZoomable: Boolean,
    portrait: Boolean,
    boxWithConstraintsScope: BoxWithConstraintsScope,
) = if (actuallyZoomable) {
    boxWithConstraintsScope.zoomableModifier(portrait, this)
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
private fun BoxWithConstraintsScope.zoomableModifier(
    portrait: Boolean,
    modifier: Modifier,
): Modifier {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(1.0f, 4.0f)

        val (width, height) = calculateWidthAndHeight(portrait)

        val scaledWidth = scale * width
        val scaledHeight = scale * height

        val maxOffsetX = (scaledWidth - constraints.maxWidth).coerceAtLeast(0f) / 2
        val maxOffsetY = (scaledHeight - constraints.maxHeight).coerceAtLeast(0f) / 2

        println("Scaled width $scaledWidth Max Width ${constraints.maxWidth} Max Offset Y $maxOffsetX")
//        println("Portrait $portrait Scale $scale Width x height: $width x $height | maxOffset $maxOffsetX x $maxOffsetY")
        offset = Offset(
            x = (offset.x + offsetChange.x).coerceIn(-maxOffsetX, maxOffsetX),
            y = (offset.y + offsetChange.y).coerceIn(-maxOffsetY, maxOffsetY)
        )
    }

    println("Scale $scale | $offset")

    return modifier
        .transformable(state = state)
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y
        )
}

private fun BoxWithConstraintsScope.calculateWidthAndHeight(
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
        sourceInfo = SourceInfo("Doesn't matter"),
        contentDescription = null,
        loadingIndicatorConfig = LoadingIndicatorConfig(
            title = "A Trip to Alivel Mall",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 0,
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        fillMaxWidth = true,
        actuallyZoomable = true,
        showDebug = true,
        modifier = Modifier.fillMaxSize(),
        portrait = true,
        actionSink = PreviewActionSink { },
    )
}
