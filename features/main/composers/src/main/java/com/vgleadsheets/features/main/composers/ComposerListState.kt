package com.vgleadsheets.features.main.composers

import com.vgleadsheets.features.main.list.BetterCompositeState

data class ComposerListState(
    override val contentLoad: ComposerListContent = ComposerListContent(),
) : BetterCompositeState<ComposerListContent>
