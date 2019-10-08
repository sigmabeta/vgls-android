package com.vgleadsheets.components

data class GiantBombTitleListModel(
    override val dataId: Long,
    val title: String,
    val subtitle: String,
    val giantBombId: Long,
    val photoUrl: String?,
    val handler: EventHandler
) : ListModel {

    override val layoutId: Int = R.layout.list_component_giantbomb_title

    interface EventHandler {
        fun onGbGameNotChecked(vglsId: Long, name: String, type: String)
    }
}
