package com.vgleadsheets.games

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.model.game.Game

data class GameListState(val games: Async<List<Game>> = Uninitialized) : MvRxState