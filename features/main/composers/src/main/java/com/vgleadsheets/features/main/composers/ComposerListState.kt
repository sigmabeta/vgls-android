package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.model.composer.Composer

data class ComposerListState(
    val composers: Async<List<Composer>> = Uninitialized
) : MvRxState
