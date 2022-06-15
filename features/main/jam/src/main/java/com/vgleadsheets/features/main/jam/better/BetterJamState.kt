package com.vgleadsheets.features.main.jam.better

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterJamState(
    val jamId: Long,
    val deletion: Async<Unit> = Uninitialized,
    override val contentLoad: BetterJamContent = BetterJamContent(
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<BetterJamContent> {
    constructor(args: IdArgs) : this(args.id)
}
