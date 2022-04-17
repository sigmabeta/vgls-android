package com.vgleadsheets.features.main.tagkeys

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.ListState
import com.vgleadsheets.features.main.list.R
import com.vgleadsheets.model.tag.TagKey

data class TagKeyState(
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: PartSelectorItem? = PartSelectorItem(
        "C",
        R.string.part_c,
        R.string.part_long_c,
        true
    ),
    override val listModels: List<ListModel> = emptyList(),
    override val data: Async<List<TagKey>> = Uninitialized,
    val clickedListModel: NameCaptionListModel? = null
) : ListState<TagKey>() {
    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?,
        listModels: List<ListModel>,
        data: Async<List<TagKey>>
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        data = data
    )
}
