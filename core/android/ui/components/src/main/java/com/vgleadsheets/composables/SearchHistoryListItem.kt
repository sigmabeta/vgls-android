package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.SearchHistoryListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun SearchHistoryListItem(
    model: SearchHistoryListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(padding)
            .clickable { actionSink.sendAction(model.clickAction) },
    ) {
        val contentColor = MaterialTheme.colorScheme.onBackground

        Text(
            text = model.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1.0f)
                .padding(vertical = 12.dp)
        )
        IconButton(
            onClick = { actionSink.sendAction(model.removeAction) }
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                tint = contentColor,
                contentDescription = null,
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
@Suppress("MagicNumber")
private fun Sample() {
    SearchHistoryListItem(
        SearchHistoryListModel(
            1234L,
            "Stickerbush symphony",
            VglsAction.Noop,
            VglsAction.Noop,
        ),
        PreviewActionSink { },
        Modifier,
        PaddingValues(horizontal = 8.dp)
    )
}
