package com.vgleadsheets.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.MenuSearchMoreListModel
import com.vgleadsheets.components.R
import com.vgleadsheets.themes.VglsMaterialMenu

@Composable
fun MoreResultsLink(
    model: MenuSearchMoreListModel,
    modifier: Modifier,
) {
    Text(
        text = model.text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
            .clickable(onClick = model.onClick)
            .padding(horizontal = dimensionResource(id = R.dimen.margin_side))
            .padding(start = 56.dp)
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    )
}

@Preview
@Composable
private fun Default() {
    VglsMaterialMenu {
        Column(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            ImageNameCaptionListItem(
                ImageNameCaptionListModel(
                    1234L,
                    "Xenoblade Chronicles 3",
                    "Yasunori Mitsuda, Mariam Abounnasr, Manami Kiyota, ACE+, Kenji Hiramatsu",
                    "https://randomfox.ca/images/12.jpg",
                    R.drawable.ic_person_24dp,
                    null,
                    {}
                ),
                Modifier
            )
            Sample()
        }
    }
}

@Composable
private fun Sample() {
    MoreResultsLink(
        MenuSearchMoreListModel(
            "Show 69 more games...",
            {}
        ),
        Modifier
    )
}
