package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.Game

data class GameDetailContent(
    val game: Async<Game> = Uninitialized,
) : ListContent {
    // TODO CompositeException
    override fun failure() = game.failure()

    override fun isLoading() = game.isLoading()

    override fun hasFailed() = game.hasFailed()

    override fun isFullyLoaded() = game.isReady()

    override fun isReady() = game.isReady()

    override fun isEmpty() = game()?.songs?.isEmpty() == true
}
