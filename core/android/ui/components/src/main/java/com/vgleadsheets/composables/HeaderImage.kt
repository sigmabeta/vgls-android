package com.vgleadsheets.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.ui.Icon

@Composable
fun HeaderImage(
    sourceInfo: SourceInfo = SourceInfo(null),
    imagePlaceholder: Icon,
    modifier: Modifier
) {
    CrossfadeImage(
        sourceInfo = sourceInfo,
        imagePlaceholder = imagePlaceholder,
        modifier = modifier,
    )
}

@Composable
fun HeaderImage(
    model: HeroImageListModel,
    modifier: Modifier
) {
    HeaderImage(
        sourceInfo = model.sourceInfo,
        imagePlaceholder = model.imagePlaceholder,
        modifier = modifier
    )
}

@Preview
@Composable
private fun LoadingGame() {
    HeaderImage(
        HeroImageListModel(
            sourceInfo = SourceInfo("whatever"),
            imagePlaceholder = Icon.ALBUM,
            clickAction = VglsAction.Noop,
        ),
        modifier = Modifier
            .height(320.dp)
            .fillMaxWidth(),
    )
}

@Preview
@Composable
private fun SuccessGame() {
    HeaderImage(
        HeroImageListModel(
            sourceInfo = SourceInfo("whatever"),
            imagePlaceholder = Icon.DESCRIPTION,
            clickAction = VglsAction.Noop,
        ),
        modifier = Modifier
            .fillMaxWidth(),
    )
}
