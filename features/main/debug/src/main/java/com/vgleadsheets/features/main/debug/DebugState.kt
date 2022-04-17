package com.vgleadsheets.features.main.debug

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.async.AsyncListState

data class DebugState(
    val sheetDeletion: Async<Unit> = Uninitialized,
    val jamDeletion: Async<Unit> = Uninitialized,
    val changed: Boolean = false,
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: PartSelectorItem? = PartSelectorItem(
        "C",
        R.string.part_c,
        R.string.part_long_c,
        true
    ),
    override val listModels: List<ListModel> = emptyList(),
    override val data: DebugData = DebugData()
) : AsyncListState<DebugData>(data = data) {
    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?,
        listModels: List<ListModel>,
        data: DebugData
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        data = data
    )
}
