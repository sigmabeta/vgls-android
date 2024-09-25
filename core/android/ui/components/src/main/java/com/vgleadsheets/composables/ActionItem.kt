package com.vgleadsheets.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.id
import com.vgleadsheets.ui.themes.VglsMaterial
import com.vgleadsheets.ui.themes.VglsMaterialMenu

@Composable
fun ActionItem(
    model: CtaListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(padding)
            .clickable(
                onClick = { actionSink.sendAction(model.clickAction) },
            )
    ) {
        Icon(
            painterResource(id = model.icon.id()),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(vertical = 12.dp)
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = model.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 16.dp
                )
        )
    }
}

@Preview
@Composable
private fun Selected() {
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
private fun NotMenu() {
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
    ActionItem(
        CtaListModel(
            Icon.JAM_EMPTY,
            "Find a path to the future",
            VglsAction.Noop,
        ),
        PreviewActionSink { },
        padding = PaddingValues(horizontal = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.margin_medium)),
        modifier = Modifier
    )
}
