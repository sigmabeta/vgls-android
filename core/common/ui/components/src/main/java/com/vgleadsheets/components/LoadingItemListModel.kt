package com.vgleadsheets.components

data class LoadingItemListModel(
    val loadingType: LoadingType,
    val loadOperationName: String,
    val loadPositionOffset: Int
) : ListModel() {
    override val dataId = "${this.javaClass.simpleName}.$loadOperationName".hashCode().toLong() + loadPositionOffset
    override val columns = when (loadingType) {
        LoadingType.SHEET, LoadingType.SQUARE, LoadingType.NOTIF, LoadingType.WIDE_ITEM -> 1
        else -> COLUMNS_ALL
    }
}
