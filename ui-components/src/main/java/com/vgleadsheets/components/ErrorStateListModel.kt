package com.vgleadsheets.components

data class ErrorStateListModel(
    val failedOperationName: String,
    val errorString: String,
    val screenName: String,
    val handler: EventHandler
) : ListModel {
    interface EventHandler {
        fun onErrorStateLoadComplete(screenName: String)
    }

    override val dataId = errorString.hashCode().toLong()
    override val layoutId = R.layout.list_component_error_state
}
