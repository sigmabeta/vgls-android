package com.vgleadsheets.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.subs.LabeledThingy
import com.vgleadsheets.strings.VglsStringId
import com.vgleadsheets.strings.text
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.components.DropdownSettingListModel
import net.sigmabeta.sage.ui.Icon as SageIcon
import net.sigmabeta.sage.ui.vector

// When expanded the row lifts off the list: a drop shadow + tonal elevation, a little breathing
// room around it, and interior padding. The card itself only pads the content's top; the sides and
// the options' background are carried by the header row and the options Surface, so the
// surfaceContainer fill runs all the way to the card edges.
private val DROPDOWN_EXPANDED_ELEVATION = 4.dp
private val DROPDOWN_EXPANDED_PADDING = 16.dp
private val DROPDOWN_EXPANDED_CONTENT_PADDING = 4.dp
private val DROPDOWN_SHAPE = RoundedCornerShape(8.dp)

/**
 * Renders a [DropdownSettingListModel] as a collapsible selector. Collapsed it reads like a
 * [LabelValueListItem] — the setting name on the left, the selected option inline on the right, a
 * trailing caret — and tapping the header dispatches [DropdownSettingListModel.onExpandClicked].
 * When expanded the row lifts (animated elevation + padding) and reveals the options, each an
 * arbitrary [net.sigmabeta.sage.components.ListModel] rendered through its own renderer via
 * [Content], on a distinct `surfaceContainer` fill. Picking one dispatches its own `clickAction`;
 * the screen collapses in response. Holds no expansion state of its own.
 */
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
    val elevation by animateDpAsState(
        targetValue = if (model.expanded) DROPDOWN_EXPANDED_ELEVATION else 0.dp,
        label = "LabelDropdownListItem.elevation",
    )
    val liftPadding by animateDpAsState(
        targetValue = if (model.expanded) DROPDOWN_EXPANDED_PADDING else 0.dp,
        label = "LabelDropdownListItem.liftPadding",
    )
    val contentPadding by animateDpAsState(
        targetValue = if (model.expanded) DROPDOWN_EXPANDED_CONTENT_PADDING else 0.dp,
        label = "LabelDropdownListItem.contentPadding",
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = DROPDOWN_SHAPE,
        shadowElevation = elevation,
        tonalElevation = elevation,
        modifier = modifier.padding(liftPadding),
    ) {
        Column(modifier = Modifier.padding(top = contentPadding)) {
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
                modifier = Modifier.padding(horizontal = contentPadding),
                padding = padding,
            )

            AnimatedVisibility(visible = model.expanded) {
                // The options sit on a distinct surfaceContainer fill that runs to the card edges;
                // the Surface's own interior padding keeps the options off those edges.
                Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
                    Column(
                        modifier = Modifier.padding(
                            start = contentPadding,
                            end = contentPadding,
                            bottom = contentPadding,
                        ),
                    ) {
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
    }
}
