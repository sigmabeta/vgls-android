package com.vgleadsheets.composables.subs

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.vgleadsheets.images.BitmapGenerator
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.id
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun CrossfadeImage(
    imageUrl: String?,
    imagePlaceholder: Icon,
    modifier: Modifier,
    contentScale: ContentScale? = null,
    forceGenBitmap: Boolean = LocalInspectionMode.current
) {
    val bgModifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)

    if (imageUrl == null) {
        Image(
            painter = painterResource(id = imagePlaceholder.id()),
            contentDescription = null,
            modifier = bgModifier
        )
        return
    }

    val (painter, imageModifier) = if (forceGenBitmap) {
        BitmapPainter(
            image = BitmapGenerator.generateBitmap(imageUrl),
            filterQuality = FilterQuality.None
        ) to modifier
    } else {
        val asyncPainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .placeholder(imagePlaceholder.id())
                .crossfade(false)
                .build()
        )

        asyncPainter to if (asyncPainter.state is AsyncImagePainter.State.Success) {
            modifier
        } else {
            bgModifier
        }
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
                    imageUrl = null,
                    imagePlaceholder = Icon.PERSON,
                    modifier = Modifier,
                )
            }

            ElevatedRoundRect(
                modifier = Modifier.size(64.dp),
                cornerRadius = 4.dp
            ) {
                CrossfadeImage(
                    imageUrl = "doesn't matter",
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
                    imageUrl = null,
                    imagePlaceholder = Icon.PERSON,
                    modifier = Modifier,
                )
            }

            ElevatedCircle(
                Modifier.size(64.dp)
            ) {
                CrossfadeImage(
                    imageUrl = "doesn't matter",
                    imagePlaceholder = Icon.PERSON,
                    modifier = Modifier,
                )
            }
        }
    }
}
