package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.R
import com.vgleadsheets.themes.VglsMaterial
import com.vgleadsheets.themes.VglsMaterialMenu

@Composable
fun LabelDropdownListItem(
    model: DropdownSettingListModel,
    defaultExpansion: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.margin_side))
    ) {
        Text(
            text = model.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .weight(1.0f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var expanded by remember { mutableStateOf(defaultExpansion) }

        Box(
            modifier = Modifier
                .animateContentSize()
                .padding(vertical = 16.dp)
                .heightIn(max = 128.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            if (expanded) {
                DropdownMenu(
                    expanded = true,
                    onDismissRequest = { expanded = false },
                ) {
                    model.settingsLabels.forEachIndexed { index, label ->
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                model.onNewOptionSelected(index)
                            },
                            text = {
                                DropdownItem(
                                    value = label,
                                    selected = index == model.selectedPosition,
                                )
                            }
                        )
                    }
                }
            }

            DropdownItem(
                value = model.settingsLabels[model.selectedPosition],
                selected = true,
                modifier = Modifier.clickable { expanded = true }
            )
        }
    }
}

@Composable
private fun DropdownItem(
    value: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val color = if (selected) {
        MaterialTheme.colorScheme.onTertiaryContainer
    } else {
        MaterialTheme.colorScheme.onBackground
    }

    Text(
        text = value,
        textAlign = TextAlign.End,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .animateContentSize()
            .padding(8.dp)
    )
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
            Sample(false)
        }
    }
}

@Preview
@Composable
private fun LightExpanded() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(true)
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
            Sample(false)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DarkExpanded() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(true)
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
            Sample(false)
        }
    }
}

@Preview
@Composable
private fun MenuExpanded() {
    VglsMaterialMenu {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(true)
        }
    }
}

@Composable
private fun Sample(expanded: Boolean) {
    var selectedPosition by remember { mutableStateOf(3) }
    LabelDropdownListItem(
        defaultExpansion = expanded,
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
        ) { selectedPosition = it }
    )
}
