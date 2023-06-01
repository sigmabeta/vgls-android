package com.vgleadsheets.features.main.composers

import com.vgleadsheets.features.main.list.CompositeState

data class ComposerListState(
    override val contentLoad: ComposerListContent = ComposerListContent(),
) : CompositeState<ComposerListContent>
