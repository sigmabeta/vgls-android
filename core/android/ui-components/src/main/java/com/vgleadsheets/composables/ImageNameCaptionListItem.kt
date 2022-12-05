package com.vgleadsheets.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vgleadsheets.components.R
import com.vgleadsheets.images.previewPlaceholder
import com.vgleadsheets.themes.VglsMaterial

@Composable
fun ImageNameCaptionListItem(
    name: String,
    caption: String,
    imageUrl: String?,
    @DrawableRes imagePlaceholder: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            placeholder = previewPlaceholder(
                previewPlaceholderId = R.drawable.img_preview_game,
                realPlaceholderId = imagePlaceholder
            ),
            modifier = Modifier
                .width(48.dp)
                .aspectRatio(1.0f)
                .clip(CircleShape)
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = caption,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun Light() {
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
fun Dark() {
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

@Composable
private fun Sample() {
    ImageNameCaptionListItem(
        "Xenoblade Chronicles 3",
        "Yasunori Mitsuda, Mariam Abounnasr, Manami Kiyota, ACE+, Kenji Hiramatsu",
        "https://randomfox.ca/images/12.jpg",
        R.drawable.ic_album_24dp,
        {}
    )
}
