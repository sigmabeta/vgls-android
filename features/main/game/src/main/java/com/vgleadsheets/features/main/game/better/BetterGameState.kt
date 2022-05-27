package com.vgleadsheets.features.main.game.better

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterGameState(
    val gameId: Long,
    override val contentLoad: BetterGameContent = BetterGameContent(
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<BetterGameContent> {
    constructor(args: IdArgs) : this(args.id)
}
