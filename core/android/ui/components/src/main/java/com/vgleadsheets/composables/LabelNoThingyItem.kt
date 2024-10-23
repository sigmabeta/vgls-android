package com.vgleadsheets.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.composables.subs.LabeledThingy
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.id

@Composable
fun LabelNoThingyItem(
    model: SingleTextListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val action = model.clickAction
    val onClickLabel = if (action !is VglsAction.Noop) {
        stringResource(StringId.ACCY_OCL_SINGLE_LINE.id(), model.name)
    } else {
        null
    }
    LabeledThingy(
        label = model.name,
        thingy = {},
        onClick = { actionSink.sendAction(action) },
        onClickLabel = onClickLabel,
        modifier = modifier,
        padding = padding,
    )
}
