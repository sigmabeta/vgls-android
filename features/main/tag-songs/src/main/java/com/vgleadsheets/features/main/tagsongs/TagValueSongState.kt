package com.vgleadsheets.features.main.tagsongs

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.BetterCompositeState

data class TagValueSongState(
    val tagValueId: Long,
    override val contentLoad: Content = Content(
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<Content> {
    constructor(args: IdArgs) : this(args.id)
}
