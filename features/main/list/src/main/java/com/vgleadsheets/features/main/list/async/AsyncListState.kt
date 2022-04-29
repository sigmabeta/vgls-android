package com.vgleadsheets.features.main.list.async

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.model.parts.Part

abstract class AsyncListState<DataType : ListData>(
    open val updateTime: Async<*> = Uninitialized,
    open val digest: Async<*> = Uninitialized,
    open val selectedPart: Part = Part.C,
    open val listModels: List<ListModel> = emptyList(),
    open val data: DataType
) : MvRxState {
    abstract fun updateListState(
        updateTime: Async<*> = this.updateTime,
        digest: Async<*> = this.digest,
        selectedPart: Part = this.selectedPart,
        listModels: List<ListModel> = this.listModels,
        data: DataType = this.data
    ): AsyncListState<DataType>
}
