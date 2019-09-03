package com.vgleadsheets.features.main.viewer

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.SongArgs
import com.vgleadsheets.repository.Data

data class ViewerState(
    val songId: Long,
    val data: Async<Data<String>> = Uninitialized
) : MvRxState {
    constructor(songArgs: SongArgs) : this(songArgs.songId)
}
