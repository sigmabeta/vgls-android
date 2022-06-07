package com.vgleadsheets.features.main.tagsongs.better

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterTagValueSongState(
    val tagValueId: Long,
    override val contentLoad: BetterTagValueSongContent = BetterTagValueSongContent(
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<BetterTagValueSongContent> {
    constructor(args: IdArgs) : this(args.id)
}
