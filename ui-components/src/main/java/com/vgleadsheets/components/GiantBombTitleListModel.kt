package com.vgleadsheets.components

data class GiantBombTitleListModel(
    override val dataId: Long,
    val giantBombId: Long?,
    val title: String,
    val subtitle: String,
    val photoUrl: String?,
    val handler: EventHandler
) : ListModel {

    override val layoutId: Int = R.layout.list_component_giantbomb_title

    interface EventHandler {
        fun onGbModelNotChecked(vglsId: Long, name: String)
    }
}
