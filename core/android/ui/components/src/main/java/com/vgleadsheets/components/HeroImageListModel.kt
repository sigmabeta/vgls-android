package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.BigImage
import com.vgleadsheets.ui.components.R

data class HeroImageListModel(
    val imageUrl: String,
    val imagePlaceholder: Int,
    val name: String? = null,
    val caption: String? = null,
    val onClick: () -> Unit
) : ListModel {
    override val dataId = imageUrl.hashCode().toLong()
    override val layoutId = R.layout.list_component_cta
    override val columns = ListModel.COLUMNS_ALL

    @Composable
    override fun Content(modifier: Modifier) {
        BigImage(
            model = this,
            modifier = modifier
        )
    }
}
