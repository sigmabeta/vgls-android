package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.subs.Dropdown
import com.vgleadsheets.composables.subs.LabeledThingy
import com.vgleadsheets.strings.StringId
import com.vgleadsheets.strings.id
import com.vgleadsheets.ui.theme.AppTheme
import com.vgleadsheets.ui.theme.AppThemeMenu
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.components.DropdownSettingListModel

@Composable
fun LabelDropdownListItem(
    model: DropdownSettingListModel,
    defaultExpansion: Boolean = false,
    modifier: Modifier,
    padding: PaddingValues,
) {
    LabeledThingy(
        label = model.name,
        thingy = {
            Dropdown(
                defaultExpansion = defaultExpansion,
                selectedPosition = model.selectedPosition,
                settingsLabels = model.settingsLabels,
                onNewOptionSelected = model.onNewOptionSelected
            )
        },
        onClick = {},
        onClickLabel = stringResource(StringId.ACCY_OCL_DROPDOWN.id()),
        modifier = modifier,
        padding = padding,
    )
}

@Preview
@Composable
private fun Light() {
    AppTheme {
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
    AppTheme {
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
    AppTheme {
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
    AppTheme {
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
    AppThemeMenu {
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
    AppThemeMenu {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(true)
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun Sample(expanded: Boolean) {
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
            ).toImmutableList()
        ) { selectedPosition = it },
        defaultExpansion = expanded,
        modifier = Modifier,
        padding = PaddingValues(horizontal = 8.dp)
    )
}
