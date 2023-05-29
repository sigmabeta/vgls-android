package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.BigImage

data class HeroImageListModel(
    val imageUrl: String,
    val imagePlaceholder: Int,
) : ListModel {
    override val dataId = imageUrl.hashCode().toLong()
    override val layoutId = R.layout.list_component_cta

    @Composable
    override fun Content(modifier: Modifier) {
        BigImage(
            model = this,
            modifier = modifier
        )
    }
}
