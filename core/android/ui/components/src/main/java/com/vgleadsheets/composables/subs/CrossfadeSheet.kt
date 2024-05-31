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
private fun PortraitSheet() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            SampleSheet()
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
            "A Trip to Alivel Mall",
            "C",
            "Kirby and the Forgotten Land",
            listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun SampleSheet() {
    CrossfadeSheet(
        sourceInfo = "nope",
        pagePreview = PagePreview(
            "A Trip to Alivel Mall",
            "C",
            "Kirby and the Forgotten Land",
            listOf(
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
            "A Trip to Alivel Mall",
            "C",
            "Kirby and the Forgotten Land",
            listOf(
                "Hirokazu Ando",
            ).toImmutableList()
        ),
        sheetId = 1234L,
        modifier = Modifier.fillMaxWidth(),
        simulateError = true,
    )
}
