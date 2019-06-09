package com.vgleadsheets.sheets

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.IdArgs
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.repository.Data

data class SheetListState(
    val gameId: Long,
    val data: Async<Data<List<Song>>> = Uninitialized
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}