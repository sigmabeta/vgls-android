package com.vgleadsheets.features.main.tagsongs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.async.AsyncListState
import com.vgleadsheets.perf.tracking.common.LoadStatus

data class TagValueSongListState(
    val tagValueId: Long,
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: PartSelectorItem? = PartSelectorItem("C", R.string.part_c, true),
    override val listModels: List<ListModel> = emptyList(),
    override val loadStatus: LoadStatus = LoadStatus(),
    override val data: TagValueSongListData = TagValueSongListData(),
    val clickedListModel: ImageNameCaptionListModel? = null
) : AsyncListState<TagValueSongListData>(data = data) {
    constructor(idArgs: IdArgs) : this(idArgs.id)

    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?,
        listModels: List<ListModel>,
        loadStatus: LoadStatus,
        data: TagValueSongListData
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        loadStatus = loadStatus,
        data = data
    )
}
