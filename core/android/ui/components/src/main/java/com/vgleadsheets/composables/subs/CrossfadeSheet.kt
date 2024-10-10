package com.vgleadsheets.composables.subs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.composables.EmptyListIndicator
import com.vgleadsheets.composables.previews.PreviewSheet
import com.vgleadsheets.composables.previews.SheetConstants
import com.vgleadsheets.images.LoadingIndicatorConfig
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.toImmutableList

@Composable
@Suppress("LongMethod", "ReturnCount")
fun CrossfadeSheet(
    sourceInfo: SourceInfo,
    loadingIndicatorConfig: LoadingIndicatorConfig,
    sheetId: Long,
    fillMaxWidth: Boolean,
    showDebug: Boolean,
    modifier: Modifier,
    simulateError: Boolean = false
) {
    if (simulateError) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            ErrorState(
                SourceInfo(sourceInfo ?: "Simulated Error"),
                modifier,
                showDebug,
                IllegalArgumentException("Oops it didn't work.")
            )
        }
        return
    }

    if (LocalInspectionMode.current) {
        PreviewSheet(
            loadingIndicatorConfig,
            fillMaxWidth,
            modifier
        )
        return
    }

    if (sourceInfo == null) {
        PlaceholderSheet(
            loadingIndicatorConfig = loadingIndicatorConfig,
            seed = sheetId,
            modifier = modifier
        )
        return
    }

    SubcomposeAsyncImage(
        model = with(ImageRequest.Builder(LocalContext.current)) {
            data(sourceInfo.info)
            build()
        },
        contentScale = ContentScale.Fit,
        contentDescription = null,
        modifier = modifier
            .defaultMinSize(minWidth = SheetConstants.MIN_WIDTH)
            .aspectRatio(SheetConstants.ASPECT_RATIO),
    ) {
        val state = painter.state
        when (state) {
            is AsyncImagePainter.State.Loading ->
                PlaceholderSheet(
                    loadingIndicatorConfig = loadingIndicatorConfig,
                    seed = sheetId,
                    modifier = modifier
                )

            is AsyncImagePainter.State.Success -> {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = modifier,
                )
            }

            is AsyncImagePainter.State.Error -> {
                ErrorState(
                    sourceInfo = sourceInfo,
                    modifier = modifier,
                    showDebug = showDebug,
                    error = state.result.throwable
                )
            }

            else -> {}
        }
    }
}

@Composable
private fun ErrorState(
    sourceInfo: SourceInfo,
    modifier: Modifier,
    showDebug: Boolean,
    error: Throwable
) {
    EmptyListIndicator(
        model = ErrorStateListModel(
            failedOperationName = sourceInfo.toString(),
            errorString = "Can't load this sheet. Check your network connection and try again?",
            error = error
        ),
        showDebug = showDebug,
        modifier = modifier
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
        sourceInfo = SourceInfo("Doesn't matter"),
        loadingIndicatorConfig = LoadingIndicatorConfig(
            title = "A Trip to Alivel Mall",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 0,
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        modifier = Modifier.fillMaxSize(),
        showDebug = true,
        fillMaxWidth = true,
    )
}

@Composable
private fun SampleSheetPageOne() {
    CrossfadeSheet(
        sourceInfo = SourceInfo("nope"),
        loadingIndicatorConfig = LoadingIndicatorConfig(
            title = "A Trip to Alivel Mall",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 0,
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        modifier = Modifier.fillMaxSize(),
        showDebug = true,
        fillMaxWidth = true,
    )
}

@Composable
private fun SampleSheetPageTwo() {
    CrossfadeSheet(
        sourceInfo = SourceInfo("nope"),
        loadingIndicatorConfig = LoadingIndicatorConfig(
            title = "A Trip to Alivel Mall",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 1,
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        modifier = Modifier.fillMaxSize(),
        showDebug = true,
        fillMaxWidth = true,
    )
}

@Composable
private fun SampleError() {
    CrossfadeSheet(
        sourceInfo = SourceInfo("nope"),
        loadingIndicatorConfig = LoadingIndicatorConfig(
            title = "A Trip to Alivel Mall",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 0,
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        modifier = Modifier.fillMaxWidth(),
        simulateError = true,
        showDebug = true,
        fillMaxWidth = true,
    )
}
