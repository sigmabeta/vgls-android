package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.composables.subs.LabeledThingy
import com.vgleadsheets.composables.subs.TextValue
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.ui.themes.VglsMaterial
import com.vgleadsheets.ui.themes.VglsMaterialMenu

@Composable
fun LabelValueListItem(
    model: LabelValueListModel,
    actionHandler: (VglsAction) -> Unit,
    modifier: Modifier,
) {
    LabeledThingy(
        label = model.label,
        thingy = { TextValue(value = model.value) },
        onClick = { actionHandler(model.clickAction) },
        modifier = modifier
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
    LabelValueListItem(
        LabelValueListModel(
            "Days which are training days",
            "Every",
            VglsAction.Noop
        ),
        {},
        Modifier
    )
}
