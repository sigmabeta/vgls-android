package com.vgleadsheets.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vgleadsheets.composables.subs.LabeledThingy
import com.vgleadsheets.strings.StringId
import com.vgleadsheets.strings.id
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.SingleTextListModel

@Composable
fun LabelNoThingyItem(
    model: SingleTextListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val action = model.clickAction
    val onClickLabel = if (action !is SageAction.Noop) {
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
