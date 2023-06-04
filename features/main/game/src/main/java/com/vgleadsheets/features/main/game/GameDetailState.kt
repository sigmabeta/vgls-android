package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.CompositeState

data class GameDetailState(
    val gameId: Long,
    override val contentLoad: GameDetailContent = GameDetailContent(
        Uninitialized,
        Uninitialized
    )
) : CompositeState<GameDetailContent> {
    constructor(args: IdArgs) : this(args.id)
}
