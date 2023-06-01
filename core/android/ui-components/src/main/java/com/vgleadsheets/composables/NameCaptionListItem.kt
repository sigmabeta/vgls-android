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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.themes.VglsMaterial

@Composable
fun NameCaptionListItem(
    model: NameCaptionListModel,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                horizontal = dimensionResource(id = com.vgleadsheets.ui_core.R.dimen.margin_side)
            )
            .clickable(
                onClick = model.onClick,
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = model.name,
                style = MaterialTheme.typography.bodyMedium,
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
                text = model.caption,
                style = MaterialTheme.typography.labelSmall,
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

@Composable
private fun Sample() {
    NameCaptionListItem(
        NameCaptionListModel(
            1234L,
            "Xenoblade Chronicles 3",
            "Yasunori Mitsuda, Mariam Abounnasr, Manami Kiyota, ACE+, Kenji Hiramatsu",
            {}
        ),
        Modifier
    )
}
