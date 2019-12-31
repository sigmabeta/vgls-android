package com.vgleadsheets.features.main.jam

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.jam.JamEntity
import com.vgleadsheets.model.jam.SetlistEntry
import com.vgleadsheets.model.jam.SetlistEntryEntity

data class JamState(
    val jamId: Long,
    val jam: Async<Jam> = Uninitialized,
    val jamRefresh: Async<JamEntity> = Uninitialized,
    val setlist: Async<List<SetlistEntry>> = Uninitialized,
    val setlistRefresh: Async<List<SetlistEntryEntity>> = Uninitialized,
    val deletion: Async<Unit> = Uninitialized
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}
