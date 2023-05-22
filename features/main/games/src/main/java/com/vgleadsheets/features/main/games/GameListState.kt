package com.vgleadsheets.features.main.games

import com.vgleadsheets.features.main.list.CompositeState

data class GameListState(
    override val contentLoad: GameListContent = GameListContent(),
) : CompositeState<GameListContent>
