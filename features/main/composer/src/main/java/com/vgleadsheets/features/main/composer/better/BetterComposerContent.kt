package com.vgleadsheets.features.main.composer.better

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.song.Song

data class BetterComposerContent(
    val composer: Async<Composer> = Uninitialized,
    val songs: Async<List<Song>> = Uninitialized
) : ListContent {
    // TODO CompositeException
    override fun failure() = composer.failure() ?: songs.failure()

    override fun isLoading() = songs.isLoading()

    override fun hasFailed() = composer.hasFailed() || songs.hasFailed()

    override fun isFullyLoaded() = composer.isReady() && songs.isReady()

    override fun isReady() = composer.isReady()

    override fun isEmpty() = songs()?.isEmpty() == true
}
