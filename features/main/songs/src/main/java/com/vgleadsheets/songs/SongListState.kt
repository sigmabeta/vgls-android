package com.vgleadsheets.songs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.repository.Data

data class SongListState(
    val gameId: Long,
    val data: Async<Data<List<Song>>> = Uninitialized
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}
