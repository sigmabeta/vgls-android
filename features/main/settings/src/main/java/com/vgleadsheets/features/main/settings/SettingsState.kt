package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.async.AsyncListState
import com.vgleadsheets.perf.tracking.common.LoadStatus

data class SettingsState(
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: PartSelectorItem? = PartSelectorItem("C", R.string.part_c, true),
    override val listModels: List<ListModel> = emptyList(),
    @PersistState override val loadStatus: LoadStatus = LoadStatus(),
    override val data: SettingsData = SettingsData(),
    val clickedSingleTextModel: SingleTextListModel? = null
) : AsyncListState<SettingsData>(data = data) {
    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?,
        listModels: List<ListModel>,
        loadStatus: LoadStatus,
        data: SettingsData
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        loadStatus = loadStatus,
        data = data
    )
}
