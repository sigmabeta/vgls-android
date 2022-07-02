package com.vgleadsheets.features.main.tagsongs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagValue

data class Content(
    val tagValue: Async<TagValue> = Uninitialized,
    val songs: Async<List<Song>> = Uninitialized
) : ListContent {
    // TODO CompositeException
    override fun failure() = tagValue.failure() ?: songs.failure()

    override fun isLoading() = songs.isLoading()

    override fun hasFailed() = tagValue.hasFailed() || songs.hasFailed()

    override fun isFullyLoaded() = tagValue.isReady() && songs.isReady()

    override fun isReady() = tagValue.isReady()

    override fun isEmpty() = songs()?.isEmpty() == true
}
