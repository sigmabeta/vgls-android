package com.vgleadsheets.features.main.games.better

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.SimpleListContent
import com.vgleadsheets.model.game.Game

data class BetterGameListContent(
    val gamesLoad: Async<List<Game>> = Uninitialized
) : SimpleListContent<Game>(gamesLoad)
