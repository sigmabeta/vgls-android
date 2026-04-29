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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.vgleadsheets.bitmaps.SheetConstants
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.composables.EmptyListIndicator
import com.vgleadsheets.composables.previews.PreviewSheet
import com.vgleadsheets.composables.utils.ImageSize
import com.vgleadsheets.images.LoadingIndicatorConfig
import com.vgleadsheets.images.PdfSize
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.perf.BuildConfig
import com.vgleadsheets.ui.id
import net.sigmabeta.sage.ui.imageLoadErrorStringId
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.roundToInt

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
    val pdfConfigByIdWithSize = withSize(
        pdfConfigById,
        scope
    )

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
        model = with(ImageRequest.Builder(LocalContext.current)) {
            data(pdfConfigByIdWithSize)
            build()
        }
    )

    val painterState by painter.state.collectAsState()

    Crossfade(
        targetState = painterState,
        modifier = bgModifier
            .align(Alignment.Center),
    ) {
        when (it) {
            is AsyncImagePainter.State.Success -> {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.None,
                )
            }

            is AsyncImagePainter.State.Error -> {
                ErrorState(
                    pdfConfigById = pdfConfigByIdWithSize,
                    showDebug = showDebug,
                    loadingIndicatorConfig = loadingIndicatorConfigWithSize,
                    sheetId = sheetId,
                    error = it.result.throwable,
                    modifier = Modifier,
                ) {
                    painter.restart()
                }
            }

            else -> PlaceholderSheet(
                loadingIndicatorConfig = loadingIndicatorConfigWithSize,
                seed = sheetId,
                modifier = Modifier,
            )
        }
    }
}

@Suppress("MagicNumber")
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
            errorString = stringResource(error.imageLoadErrorStringId().id()),
            error = error
        ),
        onBlack = true,
        showDebug = showDebug,
        modifier = modifier
            .height(height)
            .aspectRatio(SheetConstants.ASPECT_RATIO)
    )
}

@Preview
@Composable
private fun PortraitTitleSheet() {
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

@Preview
@Composable
private fun PortraitOtherSheet() {
    VglsMaterial {
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
    VglsMaterial {
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
    VglsMaterial {
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
