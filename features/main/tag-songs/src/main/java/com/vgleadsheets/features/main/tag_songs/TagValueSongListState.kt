package com.vgleadsheets.features.main.tag_songs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagValue

data class TagValueSongListState(
    val tagValueId: Long,
    val tagValue: Async<TagValue> = Uninitialized,
    val songs: Async<List<Song>> = Uninitialized
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}

