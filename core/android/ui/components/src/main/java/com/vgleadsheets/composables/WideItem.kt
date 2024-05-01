package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
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
            .defaultMinSize(minWidth = 192.dp)
            .height(64.dp)
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
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(8.dp)
                .align(CenterVertically)
        )
    }
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
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .wrapContentSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(16.dp)
    ) {
        WideItem(
            WideItemListModel(
                1234L,
                "Konami Kukeiha Club",
                "https://randomfox.ca/images/12.jpg",
                com.vgleadsheets.ui.icons.R.drawable.ic_person_24dp,
                null,
                {}
            ),
            modifier = Modifier
        )

        WideItem(
            WideItemListModel(
                1234L,
                "Masayoshi Soken",
                "https://randomfox.ca/images/12.jpg",
                com.vgleadsheets.ui.icons.R.drawable.ic_person_24dp,
                null,
                {}
            ),
            modifier = Modifier
        )
    }
}
