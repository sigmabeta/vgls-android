package com.vgleadsheets.features.main.tagsongs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.async.ListData
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagValue

data class TagValueSongListData(
    val tagValue: Async<TagValue> = Uninitialized,
    val songs: Async<List<Song>> = Uninitialized
) : ListData {
    override fun isEmpty() = !(
        tagValue is Success &&
            songs is Success &&
            songs()?.isNotEmpty() == true
        )

    override fun canShowPartialData() = true
}
