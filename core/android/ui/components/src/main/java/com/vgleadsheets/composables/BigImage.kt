package com.vgleadsheets.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.composables.subs.ElevatedRoundRect
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.ui.Icon

@Composable
@Suppress("MagicNumber", "LongMethod", "UnsafeCallOnNullableType")
fun BigImage(
    model: HeroImageListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    ElevatedRoundRect(
        modifier = modifier
            .padding(padding)
            .fillMaxWidth()
            .clickable(onClick = { actionSink.sendAction(model.clickAction) }),
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

@Preview
@Composable
private fun LoadingGame() {
    BigImage(
        HeroImageListModel(
            sourceInfo = SourceInfo("whatever"),
            imagePlaceholder = Icon.ALBUM,
            contentDescription = "",
            clickAction = VglsAction.Noop,
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
            imagePlaceholder = Icon.DESCRIPTION,
            contentDescription = "",
            clickAction = VglsAction.Noop,
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
            imagePlaceholder = Icon.DESCRIPTION,
            contentDescription = "",
            clickAction = VglsAction.Noop,
        ),
        PreviewActionSink { },
        modifier = Modifier,
        padding = PaddingValues(16.dp),
    )
}
