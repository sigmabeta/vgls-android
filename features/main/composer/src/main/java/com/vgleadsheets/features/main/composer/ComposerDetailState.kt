package com.vgleadsheets.features.main.composer

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.CompositeState

data class ComposerDetailState(
    val composerId: Long,
    override val contentLoad: ComposerDetailContent = ComposerDetailContent(
        Uninitialized,
    ),
) : CompositeState<ComposerDetailContent> {
    constructor(args: IdArgs) : this(args.id)
}
