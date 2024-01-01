package com.vgleadsheets.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.ui.components.R

@Composable
fun HeaderImage(
    imageUrl: String? = null,
    imagePlaceholder: Int,
    modifier: Modifier
) {
    CrossfadeImage(
        imageUrl = imageUrl,
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
        imageUrl = model.imageUrl,
        imagePlaceholder = model.imagePlaceholder,
        modifier = modifier
    )
}

@Preview
@Composable
private fun LoadingGame() {
    HeaderImage(
        HeroImageListModel(
            imageUrl = "whatever",
            imagePlaceholder = com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp,
        ) { },
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
            imageUrl = "whatever",
            imagePlaceholder = 0,
        ) { },
        modifier = Modifier
            .fillMaxWidth(),
    )
}
