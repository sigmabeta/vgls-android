package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.mvrx.VglsState

data class GameDetailState(
    val gameId: Long,
    val game: Async<Game> = Uninitialized,
    val songs: Async<List<Song>> = Uninitialized,
    val composers: Async<List<Composer>> = Uninitialized
) : VglsState {
    constructor(args: IdArgs) : this(args.id)

    override fun failure() = game.failure()

    override fun isLoading() = songs.isLoading()

    override fun hasFailed() = game.hasFailed()

    override fun isFullyLoaded() = game.isReady() && songs.isReady()

    override fun isReady() = game.isReady()

    override fun isEmpty() = songs()?.isEmpty() == true
}
