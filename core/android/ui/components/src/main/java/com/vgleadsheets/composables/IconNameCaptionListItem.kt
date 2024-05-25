package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
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
import com.vgleadsheets.components.IconNameCaptionListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.ui.themes.VglsMaterial
import com.vgleadsheets.ui.themes.VglsMaterialMenu

@Composable
fun IconNameCaptionListItem(
    model: IconNameCaptionListModel,
    actionSink: ActionSink,
    modifier: Modifier
) {
    IconNameCaptionListItem(
        model.name,
        model.caption,
        model.iconId,
        model.clickAction,
        actionSink,
        modifier,
    )
}

@Composable
fun IconNameCaptionListItem(
    name: String,
    caption: String,
    iconId: Int,
    clickAction: VglsAction,
    actionSink: ActionSink,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { actionSink.sendAction(clickAction) }
            .padding(
                horizontal = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.margin_side)
            )
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(48.dp)
                .padding(8.dp)
                .align(Alignment.CenterVertically)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 24.dp)
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(bottom = 12.dp)
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

@Preview
@Composable
private fun Menu() {
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
    IconNameCaptionListItem(
        IconNameCaptionListModel(
            1234L,
            "Moebius Battle",
            "ACE+",
            com.vgleadsheets.ui.icons.R.drawable.ic_baseline_music_note_24,
            clickAction = VglsAction.Noop,
        ),
        PreviewActionSink { },
        Modifier
    )
}
