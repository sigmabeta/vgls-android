package com.vgleadsheets.composables.subs

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.vgleadsheets.components.R
import com.vgleadsheets.themes.VglsMaterial

@Composable
fun CrossfadeImage(
    imageUrl: String?,
    imagePlaceholder: Int,
    modifier: Modifier
) {
    val bgModifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)

    if (imageUrl == null) {
        Image(
            painter = painterResource(id = imagePlaceholder),
            contentDescription = null,
            modifier = bgModifier
        )
        return
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .placeholder(imagePlaceholder)
            .crossfade(true)
            .build()
    )

    val imageModifier = if (painter.state is AsyncImagePainter.State.Success) {
        Modifier
    } else {
        bgModifier
    }

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = imageModifier
    )
}

@Preview
@Composable
private fun Light() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Preview
@Composable
private fun LightGame() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleGame()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DarkGame() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleGame()
        }
    }
}

@Composable
private fun Sample() {
    ElevatedCircle(
        Modifier.size(64.dp)
    ) {
        CrossfadeImage(
            imageUrl = "doesn't matter",
            imagePlaceholder = com.vgleadsheets.vectors.R.drawable.ic_person_24dp,
            modifier = Modifier,
        )
    }
}

@Composable
private fun SampleGame() {
    ElevatedCircle(
        Modifier.size(64.dp)
    ) {
        CrossfadeImage(
            imageUrl = "doesn't matter",
            imagePlaceholder = R.drawable.img_preview_game,
            modifier = Modifier,
        )
    }
}
