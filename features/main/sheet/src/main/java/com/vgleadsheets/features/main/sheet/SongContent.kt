package com.vgleadsheets.features.main.sheet

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagValue

data class SongContent(
    val song: Async<Song> = Uninitialized,
    val tagValues: Async<List<TagValue>> = Uninitialized
) : ListContent {
    // TODO CompositeException
    override fun failure() = song.failure() ?: tagValues.failure()

    override fun isLoading() = tagValues.isLoading()

    override fun hasFailed() = song.hasFailed() || tagValues.hasFailed()

    override fun isFullyLoaded() = song.isReady() && tagValues.isReady()

    override fun isReady() = song.isReady()

    override fun isEmpty() = tagValues()?.isEmpty() == true
}
