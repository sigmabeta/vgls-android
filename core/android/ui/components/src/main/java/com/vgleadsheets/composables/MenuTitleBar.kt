package com.vgleadsheets.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.MenuTitleBarListModel
import com.vgleadsheets.composables.subs.MenuActionIcon
import com.vgleadsheets.composables.subs.MenuButton
import com.vgleadsheets.ui.components.R
import com.vgleadsheets.ui.themes.VglsMaterialMenu

@Composable
fun MenuTitleBar(
    model: MenuTitleBarListModel,
    modifier: Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MenuActionIcon(
            iconId = model.iconId,
            onClick = model.onMenuButtonClick
        )

        MenuButton(
            onClick = model.onSearchButtonClick,
            label = stringResource(id = R.string.label_search),
            iconId = com.vgleadsheets.ui.icons.R.drawable.ic_search_black_24dp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        MenuButton(
            onClick = model.onChangePartClick,
            label = model.partLabel,
            iconId = com.vgleadsheets.ui.icons.R.drawable.ic_description_24dp,
            modifier = Modifier.padding(end = 32.dp)
        )
    }
}

@Preview
@Composable
private fun Default() {
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
    MenuTitleBar(
        MenuTitleBarListModel(
            "E♭ Instruments",
            com.vgleadsheets.ui.icons.R.drawable.ic_menu_24dp,
            {},
            {},
            {}
        ),
        Modifier
    )
}
