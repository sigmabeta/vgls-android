package com.vgleadsheets.features.main.list.async

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.R

abstract class AsyncListState<DataType : ListData>(
    open val updateTime: Async<*> = Uninitialized,
    open val digest: Async<*> = Uninitialized,
    open val selectedPart: PartSelectorItem? = PartSelectorItem(
        "C",
        R.string.part_c,
        R.string.part_long_c,
        true
    ),
    open val listModels: List<ListModel> = emptyList(),
    open val data: DataType
) : MvRxState {
    abstract fun updateListState(
        updateTime: Async<*> = this.updateTime,
        digest: Async<*> = this.digest,
        selectedPart: PartSelectorItem? = this.selectedPart,
        listModels: List<ListModel> = this.listModels,
        data: DataType = this.data
    ): AsyncListState<DataType>
}
