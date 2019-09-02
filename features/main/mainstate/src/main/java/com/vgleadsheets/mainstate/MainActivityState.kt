package com.vgleadsheets.mainstate

import com.airbnb.mvrx.MvRxState

data class MainActivityState(
    val searchQuery: String? = null
) : MvRxState
