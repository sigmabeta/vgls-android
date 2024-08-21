package com.vgleadsheets.components

data class LoadingItemListModel(
    val loadingType: LoadingType,
    val loadOperationName: String,
    val loadPositionOffset: Int
) : ListModel() {
    override val dataId = "${this.javaClass.simpleName}.$loadOperationName".hashCode().toLong() + loadPositionOffset
    override val columns = 1
}
