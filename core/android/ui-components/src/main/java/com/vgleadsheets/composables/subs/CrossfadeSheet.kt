package com.vgleadsheets.composables.subs

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
import com.vgleadsheets.themes.VglsMaterial

@Composable
fun CrossfadeSheet(
    imageUrl: String,
    pagePreview: PagePreview,
    sheetId: Long,
    modifier: Modifier,
    eventListener: SheetPageListModel.ImageListener
) {
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
                println("Failed to load image: ${(painter.state as AsyncImagePainter.State.Error).result.throwable.message}")
                EmptyListIndicator(
                    ErrorStateListModel(
                        imageUrl,
                        "Unable loading this sheet. Check your network connection and try again?"
                    ),
                    modifier
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

internal val NOOP_LISTENER = object : SheetPageListModel.ImageListener {
    override fun onClicked() {}
    override fun onLoadStarted() {}
    override fun onLoadComplete() {}
    override fun onLoadFailed(imageUrl: String, ex: Exception?) {}
}
