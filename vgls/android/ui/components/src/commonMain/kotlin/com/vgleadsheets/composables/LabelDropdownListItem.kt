package com.vgleadsheets.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.subs.LabeledThingy
import com.vgleadsheets.strings.VglsStringId
import com.vgleadsheets.strings.text
import com.vgleadsheets.ui.theme.AppTheme
import com.vgleadsheets.ui.theme.AppThemeMenu
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.components.DropdownSettingListModel
import net.sigmabeta.sage.ui.Icon as SageIcon
import net.sigmabeta.sage.ui.vector

@Composable
fun LabelDropdownListItem(
    model: DropdownSettingListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    // Down-caret at rest (0f), flipped to point up (180f) when expanded; animate the flip.
    val caretRotation by animateFloatAsState(
        targetValue = if (model.expanded) 180f else 0f,
        label = "LabelDropdownListItem.caretRotation",
    )

    Column(modifier = modifier) {
        LabeledThingy(
            label = model.name,
            thingy = {
                TextValue(value = model.options[model.selectedPosition].first)
                Icon(
                    imageVector = SageIcon.Caret.vector(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.rotate(caretRotation),
                )
            },
            onClick = { actionSink.sendAction(model.onExpandClicked) },
            onClickLabel = VglsStringId.ACCY_OCL_DROPDOWN.text(),
            modifier = Modifier,
            padding = padding,
        )

        AnimatedVisibility(visible = model.expanded) {
            Column {
                model.options.forEach { (_, option) ->
                    option.Content(
                        sink = actionSink,
                        debug = false,
                        mod = Modifier,
                        pad = padding,
                    )
                }
            }
        }
    }
}
