package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.SimpleListContent
import com.vgleadsheets.model.Composer

data class ComposerListContent(
    val composersLoad: Async<List<Composer>> = Uninitialized
) : SimpleListContent<Composer>(composersLoad)
