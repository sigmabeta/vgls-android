package com.vgleadsheets.composables

import com.vgleadsheets.composables.subs.platformSupportsElevationShadow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.ui.theme.AppTheme
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.WideItemListModel
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.ui.Icon

@Composable
fun WideItem(
    model: WideItemListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val shape = RoundedCornerShape(8.dp)

    val commonModifier = modifier
        .padding(padding)
        .padding(vertical = 4.dp)
        .defaultMinSize(minWidth = 192.dp)
        .height(64.dp)

    val actualModifier = if (platformSupportsElevationShadow()) {
        commonModifier.shadow(
            elevation = 4.dp,
            shape = shape
        )
    } else {
        commonModifier.clip(shape)
    }

    Row(
        modifier = actualModifier
            .clickable { actionSink.sendAction(model.clickAction) }
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        CrossfadeImage(
            sourceInfo = SourceInfo(model.sourceInfo),
            imagePlaceholder = model.imagePlaceholder,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1.0f)
        )

        Text(
            text = model.name,
            textAlign = TextAlign.Start,
            maxLines = 2,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(8.dp)
                .align(CenterVertically)
        )
    }
}
