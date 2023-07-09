package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun DetailHeader(
    model: TitleListModel,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = model.title,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        )

        Text(
            text = model.subtitle,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
                .padding(bottom = 16.dp)
        )

        Divider(
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.alpha(0.2f)
        )
    }
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

@Composable
private fun Sample() {
    DetailHeader(
        TitleListModel(
            "Baten Kaitos: Eternal Wings and the Lost Ocean",
            "15 songs",
            false,
            true,
            true,
            photoUrl = "https://randomfox.ca/images/96.jpg",
            placeholder = com.vgleadsheets.components.R.drawable.img_preview_game,
            onMenuButtonClick = { },
            onImageLoadSuccess = { },
            onImageLoadFail = { },
        ),
        Modifier
    )
}
