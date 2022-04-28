package com.vgleadsheets.features.main.jams

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.features.main.list.ListState
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.parts.Part

data class JamListState(
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: Part = Part.C,
    override val listModels: List<ListModel> = emptyList(),
    override val data: Async<List<Jam>> = Uninitialized,
    val clickedCtaModel: CtaListModel? = null,
    val clickedJamModel: NameCaptionListModel? = null
) : ListState<Jam>() {
    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: Part,
        listModels: List<ListModel>,
        data: Async<List<Jam>>
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        data = data
    )
}
