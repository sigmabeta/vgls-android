package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song

data class GameDetailContent(
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
