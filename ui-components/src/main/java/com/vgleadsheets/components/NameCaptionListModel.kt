package com.vgleadsheets.components

data class NameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val listener: ClickListener,
    val type: String = TYPE_DEFAULT,
    override val layoutId: Int = R.layout.list_component_name_caption
) : ListModel {
    interface ClickListener {
        fun onClicked(clicked: NameCaptionListModel)
    }

    companion object {
        val TYPE_DEFAULT = NameCaptionListModel::class.java.name
    }
}
