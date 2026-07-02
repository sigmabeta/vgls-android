package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.subs.LabeledThingy
import com.vgleadsheets.strings.VglsStringId
import com.vgleadsheets.strings.text
import com.vgleadsheets.ui.theme.AppTheme
import com.vgleadsheets.ui.theme.AppThemeMenu
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.components.DropdownSettingListModel
import net.sigmabeta.sage.ui.Icon as SageIcon
import net.sigmabeta.sage.ui.vector

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
    LabelDropdownListItem(
        model = DropdownSettingListModel.ofLabels(
            settingId = "",
            name = "Who the bus is",
            selectedPosition = 3,
            labels = listOf(
                "Noah",
                "Lanz",
                "Taion",
                "Eunie",
                "Mio",
                "Sena",
            ).toImmutableList(),
            expanded = expanded,
        ),
        actionSink = PreviewActionSink(),
        modifier = Modifier,
        padding = PaddingValues(horizontal = 8.dp)
    )
}
