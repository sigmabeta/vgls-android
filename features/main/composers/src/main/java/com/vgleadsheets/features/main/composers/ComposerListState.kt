package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Song
import com.vgleadsheets.mvrx.VglsState

data class ComposerListState(
    val composers: Async<List<Composer>> = Uninitialized,
    val composerToSongListMap: Async<Map<Composer, List<Song>>> = Uninitialized
) : VglsState {
    override fun failure() = composers.failure() ?: composerToSongListMap.failure()

    override fun isLoading() = composers is Loading

    override fun hasFailed() = composers is Fail

    override fun isEmpty() = composers()?.isEmpty() ?: false

    override fun isReady() = composers.isReady()

    override fun isFullyLoaded() = composers.isReady() && composerToSongListMap.isReady()
}
