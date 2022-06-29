package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.*
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.song.Song


data class GameContent(
    val game: Async<Game> = Uninitialized,
    val songs: Async<List<Song>> = Uninitialized
) : ListContent {
    // TODO CompositeException
    override fun failure() = game.failure() ?: songs.failure()

    override fun isLoading() = songs.isLoading()

    override fun hasFailed() = game.hasFailed() || songs.hasFailed()

    override fun isFullyLoaded() = game.isReady() && songs.isReady()

    override fun isReady() = game.isReady()

    override fun isEmpty() = songs()?.isEmpty() == true
}
