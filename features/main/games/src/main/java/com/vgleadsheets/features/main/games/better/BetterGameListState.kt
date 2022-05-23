package com.vgleadsheets.features.main.games.better

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.BetterListState
import com.vgleadsheets.model.game.Game

data class BetterGameListState(
    override val dataLoad: Async<List<Game>> = Uninitialized,
) : BetterListState<Game>
