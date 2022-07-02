package com.vgleadsheets.features.main.tagvalues

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.BetterCompositeState

data class TagValueState(
    val tagValueId: Long,
    override val contentLoad: TagValueContent = TagValueContent(
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<TagValueContent> {
    constructor(args: IdArgs) : this(args.id)
}
