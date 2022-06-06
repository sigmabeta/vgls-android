package com.vgleadsheets.features.main.tagvalues.better

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue

data class BetterTagValueContent(
    val tagKey: Async<TagKey> = Uninitialized,
    val tagValues: Async<List<TagValue>> = Uninitialized
) : ListContent {
    // TODO CompositeException
    override fun failure() = tagKey.failure() ?: tagValues.failure()

    override fun isLoading() = tagValues.isLoading()

    override fun hasFailed() = tagKey.hasFailed() || tagValues.hasFailed()

    override fun isFullyLoaded() = tagKey.isReady() && tagValues.isReady()

    override fun isReady() = tagKey.isReady()

    override fun isEmpty() = tagValues()?.isEmpty() == true
}
