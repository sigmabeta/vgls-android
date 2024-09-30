package com.vgleadsheets.components

data class SinglePageListModel(
    val sheetPageCardModel: SheetPageCardListModel,
) : ListModel() {
    override val dataId: Long = sheetPageCardModel.dataId
    override val columns = ListModel.COLUMNS_ALL
}
