package com.vgleadsheets.components

data class GiantBombTitleListModel(
    override val dataId: Long,
    val giantBombId: Long?,
    val title: String,
    val subtitle: String,
    val photoUrl: String?,
    val placeholder: Int,
    val handler: EventHandler
) : ListModel {

    override val layoutId = R.layout.list_component_gb_title

    interface EventHandler {
        fun onGbModelNotChecked(vglsId: Long, name: String)
    }
}
