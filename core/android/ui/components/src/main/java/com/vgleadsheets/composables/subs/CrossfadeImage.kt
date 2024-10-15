package com.vgleadsheets.composables.subs

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.vgleadsheets.images.BitmapGenerator
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.id
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun CrossfadeImage(
    sourceInfo: SourceInfo,
    imagePlaceholder: Icon,
    modifier: Modifier,
    contentScale: ContentScale? = null,
    forceGenBitmap: Boolean = LocalInspectionMode.current
) {
    val bgModifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)

    if (sourceInfo.info == null) {
        Image(
            painter = painterResource(id = imagePlaceholder.id()),
            contentDescription = null,
            modifier = bgModifier
        )
        return
    }

    val (painter, imageModifier) = if (forceGenBitmap) {
        BitmapPainter(
            // Kotlin compiler complains without .toString() here....
            image = BitmapGenerator.generateBitmap(sourceInfo.info.toString()),
            filterQuality = FilterQuality.None
        ) to modifier
    } else {
        val asyncPainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(sourceInfo.info)
                .placeholder(imagePlaceholder.id())
                .error(imagePlaceholder.id())
                .crossfade(true)
                .build()
        )

        val state by asyncPainter.state.collectAsState()
        val possibleBgModifier = if (state is AsyncImagePainter.State.Success) {
            modifier
        } else {
            bgModifier.padding(4.dp)
        }

        asyncPainter to possibleBgModifier
    }

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = contentScale ?: ContentScale.Crop,
        modifier = imageModifier,
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
                    modifier = Modifier,
                )
            }

            ElevatedCircle(
                Modifier.size(64.dp)
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo("doesn't matter"),
                    imagePlaceholder = Icon.PERSON,
                    modifier = Modifier,
                )
            }
        }
    }
}
