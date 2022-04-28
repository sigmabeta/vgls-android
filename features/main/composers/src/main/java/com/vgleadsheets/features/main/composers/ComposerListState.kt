package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.list.ListState
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.parts.Part

data class ComposerListState(
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: Part = Part.C,
    override val listModels: List<ListModel> = emptyList(),
    override val data: Async<List<Composer>> = Uninitialized,
    val clickedListModel: ImageNameCaptionListModel? = null
) : ListState<Composer>() {
    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: Part,
        listModels: List<ListModel>,
        data: Async<List<Composer>>
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        data = data
    )
}
