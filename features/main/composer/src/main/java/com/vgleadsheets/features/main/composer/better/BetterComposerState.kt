package com.vgleadsheets.features.main.composer.better

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterComposerState(
    val composerId: Long,
    override val contentLoad: BetterComposerContent = BetterComposerContent(
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<BetterComposerContent> {
    constructor(args: IdArgs) : this(args.id)
}
