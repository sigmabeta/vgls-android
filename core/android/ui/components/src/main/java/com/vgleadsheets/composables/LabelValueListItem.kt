package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.subs.ElevatedPill
import com.vgleadsheets.composables.subs.Flasher
import com.vgleadsheets.composables.subs.LabeledThingy
import com.vgleadsheets.composables.utils.nextPercentageFloat
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlin.random.Random

@Composable
fun LabelValueListItem(
    model: LabelValueListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val value = model.value
    LabeledThingy(
        label = model.label,
        thingy = {
            if (value == null) {
                LoadingTextValue(model)
            } else {
                TextValue(value = value)
            }
        },
        onClick = { actionSink.sendAction(model.clickAction) },
        modifier = modifier,
        padding = padding,
    )
}

@Composable
fun TextValue(value: String) {
    Text(
        text = value,
        textAlign = TextAlign.End,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(vertical = 16.dp)
    )
}

@Composable
private fun LoadingTextValue(model: LabelValueListModel) {
    val randomizer = Random(model.label.hashCode())
    val randomDelay = randomizer.nextInt(200)

    ElevatedPill(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .height(14.dp)
            .fillMaxWidth(
                randomizer.nextPercentageFloat(
                    minOutOfHundred = 10,
                    maxOutOfHundred = 30,
                )
            )
    ) {
        Flasher(startDelay = randomDelay)
    }
}

@Preview
@Composable
private fun Light() {
    VglsMaterial {
        Column(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
            SampleLoading()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    VglsMaterial {
        Column(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
            SampleLoading()
        }
    }
}

@Composable
private fun Sample() {
    LabelValueListItem(
        LabelValueListModel(
            "Days which are training days",
            "Every",
            VglsAction.Noop
        ),
        PreviewActionSink {},
        Modifier,
        PaddingValues(horizontal = 8.dp)
    )
}

@Composable
private fun SampleLoading() {
    LabelValueListItem(
        LabelValueListModel(
            "Please wait, now loading...",
            null,
            VglsAction.Noop
        ),
        PreviewActionSink {},
        Modifier,
        PaddingValues(horizontal = 8.dp)
    )
}
