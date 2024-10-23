package com.vgleadsheets.composables.subs

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.vgleadsheets.images.BitmapGenerator
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.id
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun CrossfadeImage(
    sourceInfo: SourceInfo,
    imagePlaceholder: Icon,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale? = null,
    forceGenBitmap: Boolean = LocalInspectionMode.current,
) {
    val bgModifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)

    if (sourceInfo.info == null) {
        PlaceHolderImage(imagePlaceholder, contentScale, bgModifier)
        return
    }

    if (forceGenBitmap) {
        FakeImage(sourceInfo, contentScale, modifier)
        return
    }

    RealImage(
        sourceInfo,
        imagePlaceholder,
        contentDescription,
        modifier,
        bgModifier,
        contentScale,
    )
}

@Composable
private fun RealImage(
    sourceInfo: SourceInfo,
    imagePlaceholder: Icon,
    contentDescription: String?,
    modifier: Modifier,
    bgModifier: Modifier,
    contentScale: ContentScale?
) {
    val asyncPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(sourceInfo.info)
            .build()
    )

    val state by asyncPainter.state.collectAsState()
    Crossfade(
        targetState = state,
        label = "Image Crossfade",
    ) { loadingState ->
        if (loadingState is AsyncImagePainter.State.Success) {
            Image(
                painter = asyncPainter,
                contentDescription = contentDescription,
                contentScale = contentScale ?: ContentScale.Crop,
                modifier = modifier,
            )
        } else {
            PlaceHolderImage(imagePlaceholder, contentScale, bgModifier)
        }
    }
}

@Composable
private fun PlaceHolderImage(
    imagePlaceholder: Icon,
    contentScale: ContentScale?,
    bgModifier: Modifier
) {
    Image(
        painter = painterResource(imagePlaceholder.id()),
        contentDescription = null,
        contentScale = contentScale ?: ContentScale.Crop,
        modifier = bgModifier,
    )
}

@Composable
private fun FakeImage(
    sourceInfo: SourceInfo,
    contentScale: ContentScale?,
    modifier: Modifier
) {
    Image(
        painter = BitmapPainter(
            // Kotlin compiler complains without .toString() here....
            image = BitmapGenerator.generateBitmap(sourceInfo.info.toString()),
            filterQuality = FilterQuality.None
        ),
        contentDescription = null,
        contentScale = contentScale ?: ContentScale.Crop,
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
private fun Sample() {
    Column(
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.background
        )
    ) {
        Row {
            ElevatedRoundRect(
                modifier = Modifier.size(64.dp),
                cornerRadius = 4.dp
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo(null),
                    imagePlaceholder = Icon.PERSON,
                    contentDescription = null,
                    modifier = Modifier,
                )
            }

            ElevatedRoundRect(
                modifier = Modifier.size(64.dp),
                cornerRadius = 4.dp
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo("doesn't matter"),
                    imagePlaceholder = Icon.PERSON,
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
                    sourceInfo = SourceInfo(null),
                    imagePlaceholder = Icon.PERSON,
                    contentDescription = null,
                    modifier = Modifier,
                )
            }

            ElevatedCircle(
                Modifier.size(64.dp)
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo("doesn't matter"),
                    imagePlaceholder = Icon.PERSON,
                    contentDescription = null,
                    modifier = Modifier,
                )
            }
        }
    }
}
