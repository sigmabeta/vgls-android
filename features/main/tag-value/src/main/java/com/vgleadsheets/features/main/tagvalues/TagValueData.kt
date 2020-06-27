package com.vgleadsheets.features.main.tagvalues

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.async.ListData
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue

data class TagValueData(
    val tagKey: Async<TagKey> = Uninitialized,
    val tagValues: Async<List<TagValue>> = Uninitialized
) : ListData {
    override fun isEmpty() = !(
            tagKey is Success &&
                    tagValues is Success &&
                    tagValues()?.isNotEmpty() == true
            )

    override fun isLoading() = tagValues is Loading

    override fun canShowPartialData() = tagKey is Success
}
