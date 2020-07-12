package com.vgleadsheets.features.main.sheet

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.async.ListData
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagValue

data class SheetDetailData(
    val song: Async<Song> = Uninitialized,
    val tagValues: Async<List<TagValue>> = Uninitialized
) : ListData {
    override fun isEmpty() = song !is Success

    override fun canShowPartialData() = true
}
