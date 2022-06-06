package com.vgleadsheets.features.main.tagvalues.better

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterTagValueState(
    val tagValueId: Long,
    override val contentLoad: BetterTagValueContent = BetterTagValueContent(
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<BetterTagValueContent> {
    constructor(args: IdArgs) : this(args.id)
}
