package com.vgleadsheets.features.main.sheet.better

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterSongState(
    val songId: Long,
    override val contentLoad: BetterSongContent = BetterSongContent(
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<BetterSongContent> {
    constructor(args: IdArgs) : this(args.id)
}
