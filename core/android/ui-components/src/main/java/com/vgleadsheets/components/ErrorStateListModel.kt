package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.EmptyListIndicator

data class ErrorStateListModel(
    val failedOperationName: String,
    val errorString: String,
) : ListModel {
    override val dataId = errorString.hashCode().toLong()
    override val layoutId = R.layout.list_component_error_state

    @Composable
    override fun Content(modifier: Modifier) {
        EmptyListIndicator(
            model = this,
            modifier = modifier
        )
    }
}
