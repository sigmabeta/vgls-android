package com.vgleadsheets.composables.subs

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.composables.ImageNameListItem
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.images.BitmapGenerator
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.pdf.PdfConfigById
import net.sigmabeta.sage.ui.Icon
import com.vgleadsheets.ui.themes.VglsMaterial
import com.vgleadsheets.ui.vector

@Composable
fun CrossfadeImage(
    sourceInfo: SourceInfo,
    imagePlaceholder: Icon,
    contentDescription: String?,
    modifier: Modifier,
    forceGenBitmap: Boolean = LocalInspectionMode.current,
    simulateError: Boolean = false,
) {
    if (sourceInfo.info == null) {
        PlaceHolderImage(imagePlaceholder, modifier)
        return
    }

    if (forceGenBitmap) {
        FakeImage(sourceInfo, modifier)
        return
    }

    RealImage(
        sourceInfo,
        imagePlaceholder,
        contentDescription,
        simulateError,
        modifier,
    )
}

@Composable
private fun RealImage(
    sourceInfo: SourceInfo,
    imagePlaceholder: Icon,
    contentDescription: String?,
    simulateError: Boolean,
    modifier: Modifier,
) {
    if (simulateError) {
        ErrorImage(imagePlaceholder, contentDescription, modifier)
        return
    }

    val asyncPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(sourceInfo.info)
            .build()
    )

    val info = sourceInfo.info
    if (info is PdfConfigById) {
        RealPdfImage(
            asyncPainter,
            info,
            imagePlaceholder,
            contentDescription,
            simulateError,
            modifier,
        )
    } else {
        RealStandardImage(
            asyncPainter,
            imagePlaceholder,
            contentDescription,
            modifier,
        )
    }
}

@Composable
fun RealStandardImage(
    asyncPainter: AsyncImagePainter,
    imagePlaceholder: Icon,
    contentDescription: String?,
    modifier: Modifier,
) {
    val state by asyncPainter.state.collectAsState()

    Crossfade(
        targetState = state,
        label = "Image Crossfade",
    ) { loadingState ->
        when (loadingState) {
            is AsyncImagePainter.State.Success -> Image(
                painter = asyncPainter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier,
            )

            is AsyncImagePainter.State.Error -> ErrorImage(
                imagePlaceholder,
                contentDescription,
                modifier
            )

            else -> PlaceHolderImage(imagePlaceholder, modifier)
        }
    }
}

@Composable
fun RealPdfImage(
    asyncPainter: AsyncImagePainter,
    pdfConfig: PdfConfigById,
    imagePlaceholder: Icon,
    contentDescription: String?,
    simulateError: Boolean,
    modifier: Modifier,
) {
    val state by asyncPainter.state.collectAsState()

    Box(
        modifier = modifier
    ) {
        Crossfade(
            targetState = state,
            label = "Image Crossfade",
            modifier = Modifier.align(Alignment.Center),
        ) { loadingState ->
            when (loadingState) {
                is AsyncImagePainter.State.Success -> Image(
                    painter = asyncPainter,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.None,
                    modifier = Modifier.align(Alignment.Center),
                )

                is AsyncImagePainter.State.Error -> ErrorImage(
                    imagePlaceholder,
                    contentDescription,
                    modifier
                )

                else -> PlaceHolderImage(imagePlaceholder, modifier)
            }
        }
    }
}

@Composable
private fun PlaceHolderImage(
    imagePlaceholder: Icon,
    modifier: Modifier
) {
    Image(
        imageVector = imagePlaceholder.vector(),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
    )
}

@Composable
private fun ErrorImage(
    imagePlaceholder: Icon,
    contentDescription: String?,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.errorContainer)
            .fillMaxSize()
            .padding(4.dp),
    ) {
        Image(
            imageVector = imagePlaceholder.vector(),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier.fillMaxSize(),
        )

        Icon(
            imageVector = Icon.CROSSOUT.vector(),
            tint = Color.Unspecified,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun FakeImage(
    sourceInfo: SourceInfo,
    modifier: Modifier
) {
    val bitmap = BitmapGenerator.generateBitmap(sourceInfo.info.toString())
    Image(
        painter = BitmapPainter(
            // Kotlin compiler complains without .toString() here....
            image = bitmap,
            filterQuality = FilterQuality.None
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Light() {
    VglsMaterial {
        Sample()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    VglsMaterial {
        Sample()
    }
}

@Composable
@Suppress("LongMethod", "MagicNumber")
private fun Sample() {
    Column(
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.background
        )
    ) {
        ImageNameListItem(
            ImageNameListModel(
                1234L,
                "Carrying the Weight of Life",
                SourceInfo(info = null),
                Icon.DESCRIPTION,
                null,
                clickAction = VglsAction.Noop,
            ),
            PreviewActionSink { },
            Modifier,
            PaddingValues(horizontal = 8.dp)
        )

        Row {
            ElevatedRoundRect(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp),
                cornerRadius = 4.dp
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo("etc"),
                    imagePlaceholder = Icon.PERSON,
                    contentDescription = null,
                    simulateError = true,
                    forceGenBitmap = false,
                    modifier = Modifier,
                )
            }

            ElevatedRoundRect(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp),
                cornerRadius = 4.dp
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo(null),
                    imagePlaceholder = Icon.DESCRIPTION,
                    contentDescription = null,
                    modifier = Modifier,
                )
            }

            ElevatedRoundRect(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp),
                cornerRadius = 4.dp
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo("doesn't matter"),
                    imagePlaceholder = Icon.DESCRIPTION,
                    contentDescription = null,
                    modifier = Modifier,
                )
            }
        }

        Row {
            ElevatedCircle(
                Modifier.size(64.dp)
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo("etc"),
                    imagePlaceholder = Icon.PERSON,
                    contentDescription = null,
                    simulateError = true,
                    forceGenBitmap = false,
                    modifier = Modifier,
                )
            }

            ElevatedCircle(
                Modifier.size(64.dp)
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo(null),
                    imagePlaceholder = Icon.DESCRIPTION,
                    contentDescription = null,
                    modifier = Modifier,
                )
            }

            ElevatedCircle(
                Modifier.size(64.dp)
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo("doesn't matter"),
                    imagePlaceholder = Icon.DESCRIPTION,
                    contentDescription = null,
                    modifier = Modifier,
                )
            }
        }
    }
}
