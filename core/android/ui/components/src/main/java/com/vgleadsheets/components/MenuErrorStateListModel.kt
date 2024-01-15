package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.EmptyListIndicator
import com.vgleadsheets.ui.components.R

data class MenuErrorStateListModel(
    val failedOperationName: String,
    val errorString: String,
) : ListModel {
    override val dataId = errorString.hashCode().toLong()
    override val layoutId = R.layout.list_component_error_state
    override val columns = ListModel.COLUMNS_ALL

    @Composable
    override fun Content(modifier: Modifier) {
        EmptyListIndicator(
            model = this,
            modifier = modifier
        )
    }
}

