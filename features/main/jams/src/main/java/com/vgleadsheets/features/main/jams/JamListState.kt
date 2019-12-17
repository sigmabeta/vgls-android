package com.vgleadsheets.features.main.jams

import com.airbnb.mvrx.MvRxState

data class JamListState(
    val jamListId: Long = 0L
) : MvRxState