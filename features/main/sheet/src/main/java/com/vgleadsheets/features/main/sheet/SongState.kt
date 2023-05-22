package com.vgleadsheets.features.main.sheet

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.CompositeState

data class SongState(
    val songId: Long,
    override val contentLoad: SongContent = SongContent(
        Uninitialized,
        Uninitialized
    ),
) : CompositeState<SongContent> {
    constructor(args: IdArgs) : this(args.id)
}
