package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.song.Song

data class GameState(
    val gameId: Long,
    val game: Async<Game> = Uninitialized,
    val songs: Async<List<Song>> = Uninitialized
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}
