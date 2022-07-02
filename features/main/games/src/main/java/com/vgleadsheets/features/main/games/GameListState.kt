package com.vgleadsheets.features.main.games

import com.vgleadsheets.features.main.list.BetterCompositeState

data class GameListState(
    override val contentLoad: GameListContent = GameListContent(),
) : BetterCompositeState<GameListContent>
