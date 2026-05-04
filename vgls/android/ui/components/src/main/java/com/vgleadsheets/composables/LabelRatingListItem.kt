package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.subs.LabeledThingy
import com.vgleadsheets.composables.subs.Rating
import com.vgleadsheets.strings.StringId
import com.vgleadsheets.strings.id
import com.vgleadsheets.ui.theme.AppTheme
import com.vgleadsheets.ui.theme.AppThemeMenu
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.LabelRatingStarListModel

@Composable
fun LabelRatingListItem(
    model: LabelRatingStarListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    LabeledThingy(
        label = model.label,
        thingy = {
            Rating(
                score = model.value,
                modifier = Modifier
            )
        },
        onClick = { actionSink.sendAction(model.clickAction) },
        onClickLabel = stringResource(StringId.ACCY_OCL_RATING.id()),
        modifier = modifier.semantics {
            stateDescription = "${model.value} out of 4"
        },
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
            Sample()
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
            Sample()
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
            Sample()
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun Sample() {
    LabelRatingListItem(
        LabelRatingStarListModel(
            "Days which are training days",
            3,
            SageAction.Noop
        ),
        PreviewActionSink { },
        Modifier,
        PaddingValues(horizontal = 8.dp)
    )
}
