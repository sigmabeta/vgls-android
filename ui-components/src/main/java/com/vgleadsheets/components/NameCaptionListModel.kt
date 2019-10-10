package com.vgleadsheets.components

data class NameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val listener: EventHandler,
    val type: String = TYPE_DEFAULT
) : ListModel {
    override val layoutId = R.layout.list_component_name_caption

    interface EventHandler {
        fun onClicked(clicked: NameCaptionListModel)
    }

    companion object {
        val TYPE_DEFAULT = NameCaptionListModel::class.java.name
    }
}
