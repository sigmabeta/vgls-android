package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.Subsection
import com.vgleadsheets.ui.components.R

data class SubsectionListModel(
    val id: Long,
    val titleModel: SubsectionHeaderListModel,
    val children: List<ListModel>
) : ListModel {
    override val dataId = titleModel.hashCode().toLong()
    override val layoutId = R.layout.composable_list_item

    @Composable
    override fun Content(modifier: Modifier) {
        Subsection(
            model = this,
            modifier = modifier
        )
    }
}
