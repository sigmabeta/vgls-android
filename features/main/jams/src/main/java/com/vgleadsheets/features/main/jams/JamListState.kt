package com.vgleadsheets.features.main.jams

import com.vgleadsheets.features.main.list.BetterCompositeState

data class JamListState(
    override val contentLoad: JamListContent = JamListContent(),
) : BetterCompositeState<JamListContent>
