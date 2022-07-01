package com.vgleadsheets.features.main.tagkeys

import com.vgleadsheets.features.main.list.BetterCompositeState

data class TagKeyListState(
    override val contentLoad: TagKeyListContent = TagKeyListContent(),
) : BetterCompositeState<TagKeyListContent>
