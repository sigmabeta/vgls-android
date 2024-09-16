package com.vgleadsheets.components

data object NoopListModel : ListModel() {
    override val dataId: Long
        get() = throw IllegalStateException("Did not filter out all NoopListModels")
    override val columns: Int
        get() = throw IllegalStateException("Did not filter out all NoopListModels")
}
