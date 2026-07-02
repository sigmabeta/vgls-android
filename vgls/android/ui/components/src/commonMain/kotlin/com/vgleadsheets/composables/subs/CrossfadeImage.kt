package com.vgleadsheets.composables.subs

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
import coil3.compose.LocalPlatformContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.vgleadsheets.composables.ImageNameListItem
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.images.BitmapGenerator
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.ImageNameListModel
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.ui.Icon
import com.vgleadsheets.ui.theme.AppTheme
import net.sigmabeta.sage.ui.vector

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
        model = ImageRequest.Builder(LocalPlatformContext.current)
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
            imageVector = Icon.CrossOut.vector(),
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
