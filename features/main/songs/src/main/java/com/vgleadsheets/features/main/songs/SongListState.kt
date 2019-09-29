package com.vgleadsheets.features.main.songs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.SongListArgs
import com.vgleadsheets.model.song.Song

data class SongListState(
    val type: SongListArgs,
    val id: Long?,
    val title: Async<String> = Uninitialized,
    val data: Async<List<Song>> = Uninitialized
) : MvRxState {
    constructor(args: SongListArgs) : this(args, args.id)
}
