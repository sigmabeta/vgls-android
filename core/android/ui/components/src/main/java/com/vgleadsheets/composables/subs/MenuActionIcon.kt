package com.vgleadsheets.composables.subs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vgleadsheets.ui.components.R

@Composable
fun MenuActionIcon(
    iconId: Int,
    onClick: () -> Unit
) {
    Icon(
        painter = painterResource(id = iconId),
        contentDescription = stringResource(id = R.string.cont_desc_app_menu),
        tint = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier
            .clickable(onClick = onClick)
            .defaultMinSize(
                minHeight = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.min_clickable_size),
                minWidth = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.min_clickable_size),
            )
            .padding(12.dp)
    )
}
