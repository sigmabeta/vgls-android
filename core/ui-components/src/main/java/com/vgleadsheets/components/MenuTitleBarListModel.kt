package com.vgleadsheets.components

data class MenuTitleBarListModel(
    val title: String,
    val subtitle: String,
    val showChangeParts: Boolean,
    val iconId: Int,
    val onMenuButtonClick: () -> Unit,
    val onChangePartClick: () -> Unit
) : ListModel {

    override val dataId = javaClass.simpleName.hashCode().toLong()

    override val layoutId = R.layout.list_component_menu_title_bar
}
