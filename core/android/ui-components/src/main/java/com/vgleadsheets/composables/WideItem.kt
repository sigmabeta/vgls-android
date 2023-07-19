package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun WideItem(
    model: WideItemListModel,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 4.dp)
            .height(54.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = model.onClick)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        CrossfadeImage(
            imageUrl = model.imageUrl,
            imagePlaceholder = model.imagePlaceholder,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1.0f)
        )

        Text(
            text = model.name,
            textAlign = TextAlign.Start,
            maxLines = 2,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1.0f)
                .padding(8.dp)
                .align(CenterVertically)
        )
    }
}

@Preview
@Composable
private fun Light() {
    VglsMaterial {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .padding(16.dp)
        ) {
            Sample()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    VglsMaterial {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .padding(16.dp)
        ) {
            Sample()
        }
    }
}

@Composable
private fun RowScope.Sample() {
    WideItem(
        WideItemListModel(
            1234L,
            "Konami Kukeiha Club",
            "https://randomfox.ca/images/12.jpg",
            com.vgleadsheets.vectors.R.drawable.ic_person_24dp,
            null,
            {}
        ),
        Modifier
    )
}
