package com.vgleadsheets.components

data class GiantBombImageNameCaptionListModel(
    override val dataId: Long,
    val giantBombId: Long?,
    val name: String,
    val caption: String,
    val photoUrl: String?,
    val imagePlaceholder: Int,
    val handler: EventHandler,
    val type: String = TYPE_DEFAULT
) : ListModel {
    interface EventHandler {
        fun onClicked(clicked: GiantBombImageNameCaptionListModel)
        fun clearClicked()
        fun onGbModelNotChecked(vglsId: Long, name: String, type: String)
        fun onGbApiNotAvailable()
    }

    override val layoutId = R.layout.list_component_gb_image_name_caption

    companion object {
        val TYPE_DEFAULT = GiantBombImageNameCaptionListModel::class.java.name
    }
}
