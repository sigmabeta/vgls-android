package com.vgleadsheets.features.main.tagkeys

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.SimpleListContent
import com.vgleadsheets.model.tag.TagKey

data class TagKeyListContent(
    val tagKeysLoad: Async<List<TagKey>> = Uninitialized
) : SimpleListContent<TagKey>(tagKeysLoad)
