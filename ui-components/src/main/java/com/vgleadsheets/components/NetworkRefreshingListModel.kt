package com.vgleadsheets.components

data class NetworkRefreshingListModel(
    val refreshType: String
) : ListModel {
    override val dataId = refreshType.hashCode().toLong()

    override val layoutId = R.layout.list_component_network_refreshing
}