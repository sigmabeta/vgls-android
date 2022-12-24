package com.vgleadsheets.composables.subs

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
) {
    val bgModifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)

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
    VglsMaterial(useDarkTheme = false) {
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
private fun Dark() {
    VglsMaterial(useDarkTheme = true) {
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
fun LightGame() {
    VglsMaterial(useDarkTheme = false) {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleGame()
        }
    }
}

@Preview
@Composable
fun DarkGame() {
    VglsMaterial(useDarkTheme = true) {
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
            imagePlaceholder = R.drawable.ic_person_24dp,
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
            imagePlaceholder = R.drawable.img_preview_game
        )
    }
}
