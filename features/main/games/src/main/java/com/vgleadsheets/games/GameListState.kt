package com.vgleadsheets.games

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.repository.Data

data class GameListState(val data: Async<Data<List<Game>>> = Uninitialized,
                         val clickedGame: Game? = null) : MvRxState