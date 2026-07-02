package com.vgleadsheets.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.ui.theme.AppTheme
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.IconNameListModel
import net.sigmabeta.sage.ui.vector

@Composable
@Suppress("LongMethod")
fun MenuItem(
    model: IconNameListModel,
    actionSink: ActionSink,
    padding: PaddingValues,
    modifier: Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(padding)
            .clickable { actionSink.sendAction(model.clickAction) },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val color = if (model.active) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onBackground
        }

        Icon(
            imageVector = model.icon.vector(),
            tint = color,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp)
                .padding(vertical = 12.dp)
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = model.name,
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = if (model.active) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier
                .weight(1.0f)
                .padding(
                    top = 16.dp,
                    bottom = 16.dp
                )
        )
    }
}
