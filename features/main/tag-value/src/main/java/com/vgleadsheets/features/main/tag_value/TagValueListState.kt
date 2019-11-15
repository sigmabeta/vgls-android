package com.vgleadsheets.features.main.tag_value_list

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue

data class TagValueListState(
    val tagKeyId: Long,
    val tagKey: Async<TagKey> = Uninitialized,
    val tagValues: Async<List<TagValue>> = Uninitialized
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}
