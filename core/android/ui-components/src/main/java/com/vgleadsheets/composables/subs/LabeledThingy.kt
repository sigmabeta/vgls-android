package com.vgleadsheets.composables.subs

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.CheckableListModel
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.LabelRatingStarListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.R
import com.vgleadsheets.composables.LabelCheckboxItem
import com.vgleadsheets.composables.LabelDropdownListItem
import com.vgleadsheets.composables.LabelRatingListItem
import com.vgleadsheets.composables.LabelValueListItem
import com.vgleadsheets.themes.VglsMaterial
import com.vgleadsheets.themes.VglsMaterialMenu

@Composable
fun LabeledThingy(
    label: String,
    thingy: @Composable RowScope.() -> Unit,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = dimensionResource(id = R.dimen.margin_side)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .weight(1.0f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        thingy()
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
    Column {
        LabelValueListItem(
            LabelValueListModel(
                "Days which are training days",
                "Every",
                {}
            ),
            Modifier
        )

        LabelRatingListItem(
            LabelRatingStarListModel(
                "Meatiness of current thing",
                3,
                {}
            ),
            Modifier
        )

        var isChecked by remember { mutableStateOf(true) }
        LabelCheckboxItem(
            CheckableListModel(
                "someId",
                "Sena seen in action",
                isChecked
            ) { isChecked = !isChecked },
            Modifier
        )

        var selectedPosition by remember { mutableStateOf(3) }
        LabelDropdownListItem(
            model = DropdownSettingListModel(
                "",
                "Who the bus is",
                selectedPosition,
                listOf(
                    "Noah",
                    "Lanz",
                    "Taion",
                    "Eunie",
                    "Mio",
                    "Sena",
                )
            ) { selectedPosition = it },
            defaultExpansion = false,
            modifier = Modifier
        )
    }
}

