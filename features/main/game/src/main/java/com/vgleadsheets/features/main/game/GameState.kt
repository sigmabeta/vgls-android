package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.BetterCompositeState

data class GameState(
    val gameId: Long,
    override val contentLoad: GameDetailContent = GameDetailContent(
        Uninitialized,
        Uninitialized
    )
) : BetterCompositeState<GameDetailContent> {
    constructor(args: IdArgs) : this(args.id)
}
