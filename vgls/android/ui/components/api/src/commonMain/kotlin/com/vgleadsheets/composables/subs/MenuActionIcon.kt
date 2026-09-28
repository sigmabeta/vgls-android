package com.vgleadsheets.composables.subs

import com.vgleadsheets.composables.Dimensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vgleadsheets.strings.VglsStringId
import com.vgleadsheets.strings.text
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.vector

@Composable
fun MenuActionIcon(
    icon: Icon,
    contentDescription: VglsStringId,
    onClick: () -> Unit
) {
    Icon(
        imageVector = icon.vector(),
        contentDescription = contentDescription.text(),
        tint = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier
            .clickable(onClick = onClick)
            .defaultMinSize(
                minHeight = Dimensions.minClickableSize,
                minWidth = Dimensions.minClickableSize,
            )
            .padding(12.dp)
    )
}
