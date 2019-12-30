package com.vgleadsheets.features.main.jams

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.model.jam.Jam

data class JamListState(
    val jamListId: Long = 0L,
    val jams: Async<List<Jam>> = Uninitialized
) : MvRxState
