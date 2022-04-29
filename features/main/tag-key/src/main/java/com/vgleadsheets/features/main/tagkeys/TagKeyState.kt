package com.vgleadsheets.features.main.tagkeys

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.features.main.list.ListState
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.tag.TagKey

data class TagKeyState(
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: Part = Part.C,
    override val listModels: List<ListModel> = emptyList(),
    override val data: Async<List<TagKey>> = Uninitialized,
    val clickedListModel: NameCaptionListModel? = null
) : ListState<TagKey>() {
    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: Part,
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
