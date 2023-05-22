package com.vgleadsheets.features.main.tagsongs

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.CompositeState

data class TagValueSongState(
    val tagValueId: Long,
    override val contentLoad: TagValueSongContent = TagValueSongContent(
        Uninitialized,
        Uninitialized
    ),
) : CompositeState<TagValueSongContent> {
    constructor(args: IdArgs) : this(args.id)
}
