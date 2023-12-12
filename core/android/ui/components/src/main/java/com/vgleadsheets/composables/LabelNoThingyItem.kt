package com.vgleadsheets.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.composables.subs.LabeledThingy

@Composable
fun LabelNoThingyItem(
    model: SingleTextListModel,
    modifier: Modifier
) {
    LabeledThingy(
        label = model.name,
        thingy = {},
        onClick = {},
        modifier = modifier
    )
}
