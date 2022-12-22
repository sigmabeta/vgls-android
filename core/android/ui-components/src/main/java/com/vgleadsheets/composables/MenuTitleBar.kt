package com.vgleadsheets.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.MenuTitleBarListModel
import com.vgleadsheets.components.R
import com.vgleadsheets.themes.VglsMaterialMenu

@Composable
fun MenuTitleBar(model: MenuTitleBarListModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = model.iconId),
            contentDescription = stringResource(id = R.string.cont_desc_app_menu),
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .clickable(onClick = model.onMenuButtonClick)
                .align(Alignment.CenterVertically)
                .defaultMinSize(
                    minHeight = dimensionResource(id = R.dimen.min_clickable_size),
                    minWidth = dimensionResource(id = R.dimen.min_clickable_size),
                )
                .padding(vertical = 12.dp)
        )

        MenuButton(
            onClick = model.onSearchButtonClick,
            label = stringResource(id = R.string.label_search),
            iconId = R.drawable.ic_search_black_24dp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        MenuButton(
            onClick = model.onChangePartClick,
            label = model.partLabel,
            iconId = R.drawable.ic_description_24dp,
            modifier = Modifier.padding(end = 32.dp)
        )
    }
}

@Composable
private fun MenuButton(
    onClick: () -> Unit,
    label: String,
    iconId: Int,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onPrimary
        ),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label
        )
    }
}

@Preview
@Composable
private fun Selected() {
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
            "E♭ Instr.",
            R.drawable.ic_menu_24dp,
            {},
            {},
            {}
        )
    )
}
