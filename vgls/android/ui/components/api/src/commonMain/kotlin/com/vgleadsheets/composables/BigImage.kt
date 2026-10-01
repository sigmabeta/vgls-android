package com.vgleadsheets.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.BigImageConstants
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.composables.subs.ElevatedRoundRect
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.HeroImageListModel
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.ui.Icon

@Composable
fun BigImage(
    model: HeroImageListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    BigImageFrame(modifier = modifier, padding = padding) { frameModifier ->
        ElevatedRoundRect(
            modifier = frameModifier.clickable(onClick = { actionSink.sendAction(model.clickAction) }),
            cornerRadius = 16.dp,
        ) {
            CrossfadeImage(
                sourceInfo = model.sourceInfo,
                imagePlaceholder = model.imagePlaceholder,
                contentDescription = model.contentDescription,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

/**
 * The hero's shape, shared with its loading placeholder: cover-shaped, as tall as
 * [BigImageConstants.MAX_HEIGHT] allows, and centered in the full width.
 */
@Composable
internal fun BigImageFrame(
    modifier: Modifier,
    padding: PaddingValues,
    content: @Composable (Modifier) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(padding)
            .fillMaxWidth(),
    ) {
        content(
            Modifier
                .heightIn(max = BigImageConstants.MAX_HEIGHT)
                .aspectRatio(BigImageConstants.ASPECT_RATIO, matchHeightConstraintsFirst = true),
        )
    }
}
