package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.CheckableListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.subs.LabeledThingy
import com.vgleadsheets.ui.themes.VglsMaterial
import com.vgleadsheets.ui.themes.VglsMaterialMenu

@Composable
fun LabelCheckboxItem(
    model: CheckableListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    LabeledThingy(
        label = model.name,
        thingy = {
            Checkbox(
                model.checked,
                { actionSink.sendAction(model.clickAction) }
            )
        },
        onClick = { actionSink.sendAction(model.clickAction) },
        modifier = modifier,
        padding = padding,
    )
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
    var isChecked by remember { mutableStateOf(true) }

    LabelCheckboxItem(
        CheckableListModel(
            "someId",
            "Sena seen in action",
            isChecked,
            clickAction = VglsAction.Noop,
        ),
        PreviewActionSink { isChecked = !isChecked },
        Modifier,
        PaddingValues(horizontal = 8.dp)
    )
}
