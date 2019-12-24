package com.vgleadsheets.components

data class NameCaptionCtaListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val ctaIconId: Int,
    val listener: EventHandler,
    val type: String = TYPE_DEFAULT
) : ListModel {
    override val layoutId = R.layout.list_component_name_caption_cta

    interface EventHandler {
        fun onClicked(clicked: NameCaptionCtaListModel)
        fun onActionClicked(clicked: NameCaptionCtaListModel)
    }

    companion object {
        val TYPE_DEFAULT = NameCaptionCtaListModel::class.java.name
    }
}
