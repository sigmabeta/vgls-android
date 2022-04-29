package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.features.main.list.async.AsyncListState
import com.vgleadsheets.model.parts.Part

data class SettingsState(
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: Part = Part.C,
    override val listModels: List<ListModel> = emptyList(),
    override val data: SettingsData = SettingsData(),
    val clickedSingleTextModel: SingleTextListModel? = null
) : AsyncListState<SettingsData>(data = data) {
    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: Part,
        listModels: List<ListModel>,
        data: SettingsData
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        data = data
    )
}
