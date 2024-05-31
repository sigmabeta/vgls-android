package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun DetailHeader(
    model: TitleListModel,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .height(256.dp)
            .fillMaxWidth()
    ) {
        CrossfadeImage(
            sourceInfo = model.photoUrl,
            imagePlaceholder = model.placeholder,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(
                        red = 0.0f,
                        green = 0.0f,
                        blue = 0.0f,
                        alpha = 0.5f
                    )
                )
        )

        Text(
            text = model.title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall.copy(
                lineHeight = TextUnit(1.2f, TextUnitType.Em),
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            ),
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(all = 16.dp)
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
            "Xenoblade Chronicles 3",
            "15 songs",
            false,
            true,
            true,
            photoUrl = "https://randomfox.ca/images/96.jpg",
            placeholder = Icon.ALBUM,
            onMenuButtonClick = { },
            onImageLoadSuccess = { },
            onImageLoadFail = { },
        ),
        Modifier
    )
}
