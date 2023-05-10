package com.vgleadsheets.features.main.favorites

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song

data class FavoriteListContent(
    val gamesLoad: Async<List<Game>> = Uninitialized,
    val composerLoad: Async<List<Composer>> = Uninitialized,
    val songsLoad: Async<List<Song>> = Uninitialized,
) : ListContent {
    // TODO CompositeException
    override fun failure(): Nothing? = null

    override fun isLoading() = false

    override fun hasFailed() = false

    override fun isFullyLoaded() = true

    override fun isReady() = true

    override fun isEmpty() = false
}

