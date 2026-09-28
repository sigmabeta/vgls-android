package com.vgleadsheets.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.composables.subs.ElevatedRoundRect
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.HeroImageListModel
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.ui.Icon

@Preview
@Composable
private fun LoadingGame() {
    BigImage(
        HeroImageListModel(
            sourceInfo = SourceInfo("whatever"),
            imagePlaceholder = Icon.Album,
            contentDescription = "",
            clickAction = SageAction.Noop,
        ),
        PreviewActionSink { },
        modifier = Modifier,
        padding = PaddingValues(16.dp),
    )
}

@Preview
@Composable
private fun SuccessGame() {
    BigImage(
        HeroImageListModel(
            sourceInfo = SourceInfo("whatever"),
            imagePlaceholder = Icon.Description,
            contentDescription = "",
            clickAction = SageAction.Noop,
        ),
        PreviewActionSink { },
        modifier = Modifier,
        padding = PaddingValues(16.dp),
    )
}

@Preview
@Composable
private fun SuccessGameWithLabel() {
    BigImage(
        HeroImageListModel(
            sourceInfo = SourceInfo("whatever"),
            imagePlaceholder = Icon.Description,
            contentDescription = "",
            clickAction = SageAction.Noop,
        ),
        PreviewActionSink { },
        modifier = Modifier,
        padding = PaddingValues(16.dp),
    )
}
