package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.HorizontalScroller

data class HorizontalScrollerListModel(
    override val dataId: Long,
    val scrollingItems: List<ListModel>,
) : ListModel {
    override val layoutId = this::class.simpleName.hashCode()
    override val columns = 1

    @Composable
    override fun Content(modifier: Modifier) {
        HorizontalScroller(
            model = this,
            modifier = modifier
        )
    }
}
