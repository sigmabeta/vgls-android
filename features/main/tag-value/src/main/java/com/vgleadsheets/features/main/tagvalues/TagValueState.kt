package com.vgleadsheets.features.main.tagvalues

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.CompositeState

data class TagValueState(
    val tagKeyId: Long,
    override val contentLoad: TagValueContent = TagValueContent(
        Uninitialized,
        Uninitialized
    ),
) : CompositeState<TagValueContent> {
    constructor(args: IdArgs) : this(args.id)
}
