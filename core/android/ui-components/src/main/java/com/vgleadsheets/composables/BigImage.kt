package com.vgleadsheets.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.R
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.composables.subs.ElevatedRoundRect

@Composable
fun BigImage(
    imageUrl: String,
    imagePlaceholder: Int,
    modifier: Modifier
) {
    ElevatedRoundRect(
        modifier = modifier
            .padding(dimensionResource(id = com.vgleadsheets.ui_core.R.dimen.margin_side))
            .height(320.dp)
            .fillMaxWidth()
    ) {
        CrossfadeImage(
            imageUrl = imageUrl,
            imagePlaceholder = imagePlaceholder,
            modifier = modifier,
        )
    }
}

@Composable
fun BigImage(
    model: HeroImageListModel,
    modifier: Modifier
) {
    BigImage(
        imageUrl = model.imageUrl,
        imagePlaceholder = model.imagePlaceholder,
        modifier = modifier
    )
}

@Preview
@Composable
fun LoadingGame() {
    BigImage(
        HeroImageListModel(
            imageUrl = "whatever",
            imagePlaceholder = com.vgleadsheets.vectors.R.drawable.ic_album_24dp,
        ),
        modifier = Modifier
    )
}

@Preview
@Composable
fun SuccessGame() {
    BigImage(
        HeroImageListModel(
            imageUrl = "whatever",
            imagePlaceholder = R.drawable.img_preview_game,
        ),
        modifier = Modifier
    )
}
