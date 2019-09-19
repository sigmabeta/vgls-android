package com.vgleadsheets.features.main.composer

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.repository.Data

data class ComposerListState(
    val data: Async<Data<List<Composer>>> = Uninitialized
) : MvRxState
