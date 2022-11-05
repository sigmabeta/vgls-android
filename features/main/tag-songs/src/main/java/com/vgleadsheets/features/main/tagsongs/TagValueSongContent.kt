package com.vgleadsheets.features.main.tagsongs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.tag.TagValue

data class TagValueSongContent(
    val tagValue: Async<TagValue> = Uninitialized,
) : ListContent {
    // TODO CompositeException
    override fun failure() = tagValue.failure()

    override fun isLoading() = tagValue.isLoading()

    override fun hasFailed() = tagValue.hasFailed()

    override fun isFullyLoaded() = tagValue.isReady()

    override fun isReady() = tagValue.isReady()

    override fun isEmpty() = tagValue()?.songs?.isEmpty() == true
}
