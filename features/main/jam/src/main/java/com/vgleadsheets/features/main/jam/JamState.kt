package com.vgleadsheets.features.main.jam

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.BetterCompositeState

data class JamState(
    val jamId: Long,
    val deletion: Async<Unit> = Uninitialized,
    override val contentLoad: JamContent = JamContent(
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<JamContent> {
    constructor(args: IdArgs) : this(args.id)
}
