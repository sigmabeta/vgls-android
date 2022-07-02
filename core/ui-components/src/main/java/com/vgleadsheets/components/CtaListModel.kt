package com.vgleadsheets.components

data class CtaListModel(
    val iconId: Int,
    val name: String,
    val onClick: () -> Unit,
) : ListModel {
    override val dataId = iconId.toLong()

    override val layoutId = R.layout.list_component_cta
}
