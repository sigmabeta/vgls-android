package com.vgleadsheets.components

data class LoadingTitleListModel(
    val loadOperationName: String = "title",
    val shouldSetMinHeightOnly: Boolean? = false,
    val shrinkPercent: Float = 0.0f,
    val hidden: Boolean = false
) : ListModel {
    // Needs to match the one in `TitleListModel` so these are considered the same view by
    // DiffUtils when loading version is replaced with the real one.
    override val dataId = R.layout.list_component_title.toLong()
    override val layoutId = R.layout.list_component_loading_title
}
