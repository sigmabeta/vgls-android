package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.BetterCompositeState

data class GameState(
    val gameId: Long,
    override val contentLoad: GameContent = GameContent(
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<GameContent> {
    ) : BetterCompositeState<Content>
    {
        constructor(args: IdArgs) : this(args.id)
    }
