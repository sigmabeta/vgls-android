package com.vgleadsheets.components

data class GiantBombImageNameCaptionListModel(
    override val dataId: Long,
    val giantBombId: Long?,
    val name: String,
    val caption: String,
    val photoUrl: String?,
    val handler: EventHandler,
    val type: String = TYPE_DEFAULT
) : ListModel {
    interface EventHandler {
        fun onClicked(clicked: GiantBombImageNameCaptionListModel)
        fun onGbGameNotChecked(vglsId: Long, name: String, type: String)
    }

    override val layoutId: Int = R.layout.list_component_giantbomb_name_caption

    companion object {
        val TYPE_DEFAULT = GiantBombImageNameCaptionListModel::class.java.name
    }
}
