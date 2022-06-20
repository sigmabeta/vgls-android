package com.vgleadsheets.features.main.search.better

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.song.Song

data class BetterSearchContent(
    val songs: Async<List<Song>> = Uninitialized,
    val composers: Async<List<Composer>> = Uninitialized,
    val games: Async<List<Game>> = Uninitialized
) : ListContent {
    // TODO CompositeException
    override fun failure(): Nothing? = null

    override fun isLoading() = false

    override fun hasFailed() = false

    override fun isFullyLoaded() = true

    override fun isReady() = true

    override fun isEmpty() = false
}
