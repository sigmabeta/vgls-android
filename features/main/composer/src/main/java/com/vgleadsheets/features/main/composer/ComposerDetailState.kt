package com.vgleadsheets.features.main.composer

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.mvrx.VglsState

data class ComposerDetailState(
    val composerId: Long,
    val composer: Async<Composer> = Uninitialized,
    val games: Async<List<Game>> = Uninitialized,
    val songs: Async<List<Song>> = Uninitialized
) : VglsState {
    constructor(args: IdArgs) : this(args.id)

    override fun failure() = composer.failure()

    override fun isLoading() = composer is Loading

    override fun hasFailed() = composer is Fail

    override fun isEmpty() = composer() == null

    override fun isReady() = composer.isReady()

    override fun isFullyLoaded() = composer.isReady() && songs.isReady()
}
