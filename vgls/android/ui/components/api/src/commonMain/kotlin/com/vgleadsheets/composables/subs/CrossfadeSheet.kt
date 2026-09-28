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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.LocalPlatformContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import kotlinx.coroutines.delay
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

private const val RESIZE_DEBOUNCE_MS = 100L

@Composable
@Suppress("LongMethod", "ReturnCount")
fun CrossfadeSheet(
    pdfConfigById: PdfConfigById,
    contentDescription: String?,
    loadingIndicatorConfig: LoadingIndicatorConfig,
    sheetId: Long,
    showDebug: Boolean,
    modifier: Modifier,
    simulateError: Boolean = false
) {
    if (pdfConfigById.pdfSize == PdfSize.FILL) {
        BoxWithConstraints {
            Content(
                pdfConfigById = pdfConfigById,
                contentDescription = contentDescription,
                loadingIndicatorConfig = loadingIndicatorConfig,
                sheetId = sheetId,
                showDebug = showDebug,
                scope = this@BoxWithConstraints,
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

@Composable
@Suppress("LongMethod")
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
    val rawPdfConfigByIdWithSize = withSize(
        pdfConfigById,
        scope
    )

    // A dynamic viewport (e.g. a resizing desktop window) emits a flurry of new sizes. Debounce them
    // so we only render the size that survives a brief quiet period; the previous size keeps being
    // requested until then, so the already-rendered sheet stays put.
    val pdfConfigByIdWithSize = rememberDebounced(rawPdfConfigByIdWithSize, RESIZE_DEBOUNCE_MS)

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

    val painter = rememberAsyncImagePainter(
        model = with(ImageRequest.Builder(LocalPlatformContext.current)) {
            data(pdfConfigByIdWithSize)
            build()
        }
    )

    val painterState by painter.state.collectAsState()

    // Once this sheet has rendered, hold onto that bitmap so a re-render at a new size doesn't flash the
    // placeholder — keep showing the last good render until the new one arrives. Reset per sheet.
    var lastSuccessPainter by remember(sheetId) { mutableStateOf<Painter?>(null) }
    LaunchedEffect(painterState) {
        val state = painterState
        if (state is AsyncImagePainter.State.Success) {
            lastSuccessPainter = state.painter
        }
    }

    // A FILL sheet (the full-screen viewer) has a container that can resize independently of the last
    // rendered bitmap, so every bitmap it shows — the current one AND any held-over one — is scaled to
    // fill and track the container as it animates. Non-FILL sheets (thumbnails) are laid out from the
    // bitmap's intrinsic size, so they draw 1:1.
    val isFill = pdfConfigByIdWithSize.pdfSize == PdfSize.FILL
    val sheetContentScale = if (isFill) ContentScale.Fit else ContentScale.None
    val sheetImageModifier = if (isFill) Modifier.fillMaxSize() else Modifier

    Crossfade(
        targetState = painterState,
        modifier = bgModifier
            .align(Alignment.Center),
    ) { state ->
        when (state) {
            is AsyncImagePainter.State.Success -> {
                Image(
                    painter = state.painter,
                    contentDescription = contentDescription,
                    contentScale = sheetContentScale,
                    modifier = sheetImageModifier,
                )
            }

            is AsyncImagePainter.State.Error -> {
                val previous = lastSuccessPainter
                if (previous != null) {
                    // Keep the last good render on screen until the new one arrives.
                    Image(
                        painter = previous,
                        contentDescription = contentDescription,
                        contentScale = sheetContentScale,
                        modifier = sheetImageModifier,
                    )
                } else {
                    ErrorState(
                        pdfConfigById = pdfConfigByIdWithSize,
                        showDebug = showDebug,
                        loadingIndicatorConfig = loadingIndicatorConfigWithSize,
                        sheetId = sheetId,
                        error = state.result.throwable,
                        modifier = Modifier,
                    ) {
                        painter.restart()
                    }
                }
            }

            else -> {
                val previous = lastSuccessPainter
                if (previous != null) {
                    // Keep the last good render on screen until the new one arrives.
                    Image(
                        painter = previous,
                        contentDescription = contentDescription,
                        contentScale = sheetContentScale,
                        modifier = sheetImageModifier,
                    )
                } else {
                    PlaceholderSheet(
                        loadingIndicatorConfig = loadingIndicatorConfigWithSize,
                        seed = sheetId,
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}

/**
 * Returns [value], but only after it has stayed unchanged for [delayMs]. The very first value is
 * returned immediately; only subsequent rapid changes (a resizing viewport) are debounced.
 */
@Composable
private fun rememberDebounced(value: PdfConfigById, delayMs: Long): PdfConfigById {
    var debounced by remember { mutableStateOf(value) }
    LaunchedEffect(value) {
        delay(delayMs)
        debounced = value
    }
    return debounced
}

@Suppress("MagicNumber")
@Composable
private fun Modifier.bgModifier(): Modifier {
    val bgColor = if (isPerfMeasurementEnabled) {
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
            errorString = error.imageLoadErrorStringId().text(),
            error = error
        ),
        onBlack = true,
        showDebug = showDebug,
        modifier = modifier
            .height(height)
            .aspectRatio(SheetConstants.ASPECT_RATIO)
    )
}
