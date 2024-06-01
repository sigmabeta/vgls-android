package com.vgleadsheets.composables.subs

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import com.vgleadsheets.bitmaps.R
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
private fun PreviewSheet(
    pagePreview: PagePreview,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.77272f)
            .background(Color.White)
    ) {
        Text(
            text = pagePreview.title,
            color = Color.Black,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp)
        )

        Text(
            text = "from ${pagePreview.gameName}",
            color = Color.Black,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 10.sp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
        )

        Text(
            text = "https://www.vgleadsheets.com/",
            color = Color.Black,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 8.sp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .padding(top = 72.dp)
                .padding(horizontal = 24.dp)
        ) {
            repeat(10) {
                Image(
                    painter = painterResource(R.drawable.img_leadsheet_single_system_blank),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(14.dp))
            }
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
