package com.vgleadsheets.features.main.composers.better

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.SimpleListContent
import com.vgleadsheets.model.composer.Composer

data class BetterComposerListContent(
    val composersLoad: Async<List<Composer>> = Uninitialized
) : SimpleListContent<Composer>(composersLoad)
