package com.vgleadsheets.features.main.tagvalues

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.tag.TagKey

data class TagValueContent(
    val tagKey: Async<TagKey> = Uninitialized
) : ListContent {
    // TODO CompositeException
    override fun failure() = tagKey.failure()

    override fun isLoading() = tagKey.isLoading()

    override fun hasFailed() = tagKey.hasFailed()

    override fun isFullyLoaded() = tagKey.isReady()

    override fun isReady() = tagKey.isReady()

    override fun isEmpty() = tagKey()?.values?.isEmpty() == true
}
