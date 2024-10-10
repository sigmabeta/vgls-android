package com.vgleadsheets.components

data class SheetPageCardListModel(
    val sheetPageModel: SheetPageListModel,
) : ListModel() {
    override val dataId: Long = sheetPageModel.dataId
    override val columns = ListModel.COLUMNS_ALL
}
