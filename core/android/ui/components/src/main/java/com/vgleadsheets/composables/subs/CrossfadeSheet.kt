package com.vgleadsheets.composables.subs

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.composables.EmptyListIndicator
import com.vgleadsheets.composables.previews.PreviewSheet
import com.vgleadsheets.images.PagePreview
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CrossfadeSheet(
    sourceInfo: Any,
    pagePreview: PagePreview,
    sheetId: Long,
    modifier: Modifier,
    simulateError: Boolean = false
) {
    if (simulateError) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            ErrorState(sourceInfo, modifier)
        }
        return
    }

    val configuration = LocalConfiguration.current
    val marginDp = dimensionResource(com.vgleadsheets.ui.core.R.dimen.margin_side)
    val widthDp = configuration.screenWidthDp - (2 * marginDp.value)

    val widthPx = with(LocalDensity.current) {
        widthDp.dp.toPx().toInt()
    }

    val painter = rememberAsyncImagePainter(
        model = with(ImageRequest.Builder(LocalContext.current)) {
            data(sourceInfo)
            size(Size(Dimension.Pixels(widthPx), Dimension.Undefined))
            build()
        }
    )

    if (LocalInspectionMode.current) {
        PreviewSheet(pagePreview, modifier)
        return
    }

    Crossfade(targetState = painter.state) {
        when (it) {
            is AsyncImagePainter.State.Loading ->
                PlaceholderSheet(
                    pagePreview = pagePreview,
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
                ErrorState(sourceInfo, modifier)
            }

            else -> {}
        }
    }
}

@Composable
private fun ErrorState(sourceInfo: Any, modifier: Modifier) {
    EmptyListIndicator(
        model = ErrorStateListModel(
            sourceInfo.toString(),
            "Can't load this sheet. Check your network connection and try again?"
        ),
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
        sourceInfo = "Doesn't matter",
        pagePreview = PagePreview(
            title = "A Trip to Alivel Mall",
            transposition = "C",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 0,
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun SampleSheetPageOne() {
    CrossfadeSheet(
        sourceInfo = "nope",
        pagePreview = PagePreview(
            title = "A Trip to Alivel Mall",
            transposition = "C",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 0,
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun SampleSheetPageTwo() {
    CrossfadeSheet(
        sourceInfo = "nope",
        pagePreview = PagePreview(
            title = "A Trip to Alivel Mall",
            transposition = "C",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 1,
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun SampleError() {
    CrossfadeSheet(
        sourceInfo = "nope",
        pagePreview = PagePreview(
            title = "A Trip to Alivel Mall",
            transposition = "C",
            gameName = "Kirby and the Forgotten Land",
            pageNumber = 0,
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        modifier = Modifier.fillMaxWidth(),
        simulateError = true,
    )
}
