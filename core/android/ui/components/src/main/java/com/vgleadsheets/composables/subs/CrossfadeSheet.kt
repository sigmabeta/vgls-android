package com.vgleadsheets.composables.subs

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.composables.EmptyListIndicator
import com.vgleadsheets.images.PagePreview
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun CrossfadeSheet(
    imageUrl: String,
    pagePreview: PagePreview,
    sheetId: Long,
    modifier: Modifier,
    eventListener: SheetPageListModel.ImageListener,
    simulateError: Boolean = false
) {
    if (simulateError) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            EmptyListIndicator(
                model = ErrorStateListModel(
                    imageUrl,
                    "Can't load this sheet. Check your network connection and try again?"
                ),
                modifier = modifier
                    .align(Alignment.Center)
            )
        }
        return
    }

    eventListener.onLoadStarted()
    val painter = rememberAsyncImagePainter(
        model = with(ImageRequest.Builder(LocalContext.current)) {
            data(imageUrl)
            size(Size.ORIGINAL)
            build()
        }
    )

    Crossfade(targetState = painter.state) {
        when (it) {
            is AsyncImagePainter.State.Loading ->
                PlaceholderSheet(
                    pagePreview = pagePreview,
                    seed = sheetId,
                    eventListener = eventListener,
                    modifier = modifier.fillMaxSize(),
                )

            is AsyncImagePainter.State.Success -> {
                eventListener.onLoadComplete()
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = modifier
                        .fillMaxSize(),
                )
            }

            is AsyncImagePainter.State.Error -> {
                eventListener.onLoadFailed(
                    imageUrl,
                    (painter.state as AsyncImagePainter.State.Error).result.throwable
                )
                EmptyListIndicator(
                    ErrorStateListModel(
                        imageUrl,
                        "Can't load this sheet. Check your network connection and try again?"
                    ),
                    modifier.fillMaxHeight()
                )
            }

            else -> {}
        }
    }
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
        imageUrl = "Doesn't matter",
        pagePreview = PagePreview(
            "A Trip to Alivel Mall",
            "C",
            "Kirby and the Forgotten Land",
            listOf(
                "Hirokazu Ando",
            )
        ),
        sheetId = 1234L,
        modifier = Modifier,
        eventListener = NOOP_LISTENER,
    )
}

@Composable
private fun SampleSheet() {
    CrossfadeSheet(
        imageUrl = "nope",
        pagePreview = PagePreview(
            "A Trip to Alivel Mall",
            "C",
            "Kirby and the Forgotten Land",
            listOf(
                "Hirokazu Ando",
            )
        ),
        sheetId = 1234L,
        modifier = Modifier,
        eventListener = NOOP_LISTENER,
    )
}

@Composable
private fun SampleError() {
    CrossfadeSheet(
        imageUrl = "nope",
        pagePreview = PagePreview(
            "A Trip to Alivel Mall",
            "C",
            "Kirby and the Forgotten Land",
            listOf(
                "Hirokazu Ando",
            )
        ),
        sheetId = 1234L,
        modifier = Modifier,
        eventListener = NOOP_LISTENER,
        simulateError = true,
    )
}

internal val NOOP_LISTENER = object : SheetPageListModel.ImageListener {
    override fun onClicked() {}
    override fun onLoadStarted() {}
    override fun onLoadComplete() {}
    override fun onLoadFailed(imageUrl: String, ex: Throwable?) {}
}
