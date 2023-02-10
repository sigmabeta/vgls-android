package com.vgleadsheets.composables.subs

import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.vgleadsheets.bitmaps.SheetGenerator
import com.vgleadsheets.components.R
import com.vgleadsheets.themes.VglsMaterial

@Composable
fun CrossfadeSheet(
    imageUrl: String,
    loadingBitmap: Bitmap,
    sheetId: Long,
    imagePlaceholder: Int? = null,
    forceLoadingState: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Crossfade(targetState = forceLoadingState) {
        if (it) {
            PlaceholderSheet(
                loadingBitmap,
                sheetId,
                modifier.clickable(onClick = onClick),
            )
        } else {
            val painter = rememberAsyncImagePainter(
                model = with(ImageRequest.Builder(LocalContext.current)) {
                    if (imagePlaceholder != null) {
                        placeholder(imagePlaceholder)
                    }
                    data(imageUrl)
                    build()

                }
            )

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = modifier
                    .fillMaxSize()
                    .clickable(onClick = onClick),
            )
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
    var loading by remember { mutableStateOf(true) }

    val dm = LocalContext.current.resources.displayMetrics
    val widthPixels = dm.widthPixels

    val generator = SheetGenerator(
        LocalContext.current,
        widthPixels,
        "https://www.vgleadsheets.com"
    )

    val bitmap = generator.generateLoadingSheet(
        title = "A Trip to Alivel Mall",
        transposition = "C",
        gameName = "Kirby and the Forgotten Land",
        composers = listOf(
            "Hirokazu Ando",
        ),
    )

    CrossfadeSheet(
        imageUrl = "Doesn't matter",
        loadingBitmap = bitmap,
        imagePlaceholder = R.drawable.img_preview_sheet_kirby,
        sheetId = 1234L,
        forceLoadingState = loading,
        onClick = { loading = !loading },
        modifier = Modifier,
    )
}

@Composable
private fun SampleSheet() {
    val dm = LocalContext.current.resources.displayMetrics
    val widthPixels = dm.widthPixels

    val generator = SheetGenerator(
        LocalContext.current,
        widthPixels,
        "https://www.vgleadsheets.com"
    )

    val bitmap = generator.generateLoadingSheet(
        title = "A Trip to Alivel Mall",
        transposition = "C",
        gameName = "Kirby and the Forgotten Land",
        composers = listOf(
            "Hirokazu Ando",
        ),
    )

    CrossfadeSheet(
        imageUrl = "nope",
        loadingBitmap = bitmap,
        imagePlaceholder = R.drawable.img_preview_sheet_kirby,
        sheetId = 1234L,
        forceLoadingState = false,
        onClick = { },
        modifier = Modifier,
    )
}
