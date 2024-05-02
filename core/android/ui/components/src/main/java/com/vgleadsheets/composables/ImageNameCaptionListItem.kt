package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.SearchResultListModel
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.composables.subs.ElevatedCircle
import com.vgleadsheets.ui.themes.VglsMaterial
import com.vgleadsheets.ui.themes.VglsMaterialMenu

@Composable
fun ImageNameCaptionListItem(
    model: ImageNameCaptionListModel,
    modifier: Modifier
) {
    ImageNameCaptionListItem(
        model.name,
        model.caption,
        model.imageUrl,
        model.imagePlaceholder,
        modifier,
        model.onClick
    )
}

@Composable
fun ImageNameCaptionListItem(
    model: SearchResultListModel,
    modifier: Modifier
) {
    ImageNameCaptionListItem(
        model.name,
        model.caption,
        model.imageUrl,
        model.imagePlaceholder,
        modifier,
        model.onClick
    )
}

@Composable
fun ImageNameCaptionListItem(
    name: String,
    caption: String,
    imageUrl: String?,
    imagePlaceholder: Int,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onClick)
            .padding(
                horizontal = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.margin_side)
            )
    ) {
        ElevatedCircle(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically)
        ) {
            CrossfadeImage(
                imageUrl = imageUrl,
                imagePlaceholder = imagePlaceholder,
                modifier = modifier
            )
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 24.dp)
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(bottom = 12.dp)
            )
        }
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

@Preview
@Composable
private fun Menu() {
    VglsMaterialMenu {
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
        ImageNameCaptionListModel(
            1234L,
            "Xenoblade Chronicles 3",
            "Yasunori Mitsuda, Mariam Abounnasr, Manami Kiyota, ACE+, Kenji Hiramatsu",
            "https://randomfox.ca/images/12.jpg",
            com.vgleadsheets.ui.icons.R.drawable.ic_person_24dp,
            null,
            {}
        ),
        Modifier
    )
}