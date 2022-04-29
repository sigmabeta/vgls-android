package com.vgleadsheets.features.main.songs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.list.ListState
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song

data class SongListState(
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: Part = Part.C,
    override val listModels: List<ListModel> = emptyList(),
    override val data: Async<List<Song>> = Uninitialized,
    val clickedListModel: ImageNameCaptionListModel? = null
) : ListState<Song>() {
    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: Part,
        listModels: List<ListModel>,
        data: Async<List<Song>>
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        data = data
    )
}
