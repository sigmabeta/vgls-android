package com.vgleadsheets.components

data class GiantBombTitleListModel(
    val giantBombId: Long?,
    val title: String,
    val subtitle: String,
    val photoUrl: String?,
    val placeholder: Int,
    val handler: EventHandler
) : ListModel {
    // Needs to match the one in `TitleListModel` so these are considered the same view by
    // DiffUtils when loading version is replaced with the real one.
    override val dataId = R.layout.list_component_title.toLong()
    override val layoutId = R.layout.list_component_gb_title

    interface EventHandler {
        fun onGbModelNotChecked(vglsId: Long, name: String)
    }
}
