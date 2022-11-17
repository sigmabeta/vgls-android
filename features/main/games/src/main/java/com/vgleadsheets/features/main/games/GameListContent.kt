package com.vgleadsheets.features.main.games

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.SimpleListContent
import com.vgleadsheets.model.Game

data class GameListContent(
    val gamesLoad: Async<List<Game>> = Uninitialized
) : SimpleListContent<Game>(gamesLoad)
