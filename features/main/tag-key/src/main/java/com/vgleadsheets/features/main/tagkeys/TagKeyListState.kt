package com.vgleadsheets.features.main.tagkeys

import com.vgleadsheets.features.main.list.CompositeState

data class TagKeyListState(
    override val contentLoad: TagKeyListContent = TagKeyListContent(),
) : CompositeState<TagKeyListContent>
