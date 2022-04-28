package com.vgleadsheets.features.main.jam

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.list.async.AsyncListState
import com.vgleadsheets.model.parts.Part

data class JamState(
    val jamId: Long,
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: Part = Part.C,
    override val listModels: List<ListModel> = emptyList(),
    override val data: JamData = JamData(),
    val deletion: Async<Unit> = Uninitialized,
    val clickedCurrentSongModel: ImageNameCaptionListModel? = null,
    val clickedHistoryModel: ImageNameCaptionListModel? = null,
    val clickedSetListModel: ImageNameCaptionListModel? = null,
    val clickedCtaModel: CtaListModel? = null,
    val refreshError: String? = null
) : AsyncListState<JamData>(data = data) {
    constructor(idArgs: IdArgs) : this(idArgs.id)

    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: Part,
        listModels: List<ListModel>,
        data: JamData
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        data = data
    )
}
