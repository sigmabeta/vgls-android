package com.vgleadsheets.features.main.list

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem

abstract class ListState<DataType>(
    open val updateTime: Async<*> = Uninitialized,
    open val digest: Async<*> = Uninitialized,
    open val selectedPart: PartSelectorItem? = PartSelectorItem(
        "C",
        R.string.part_c,
        R.string.part_long_c,
        true
    ),
    open val listModels: List<ListModel> = emptyList(),
    open val data: Async<List<DataType>> = Uninitialized
) : MvRxState {
    abstract fun updateListState(
        updateTime: Async<*> = this.updateTime,
        digest: Async<*> = this.digest,
        selectedPart: PartSelectorItem? = this.selectedPart,
        listModels: List<ListModel> = this.listModels,
        data: Async<List<DataType>> = this.data
    ): ListState<DataType>
}
