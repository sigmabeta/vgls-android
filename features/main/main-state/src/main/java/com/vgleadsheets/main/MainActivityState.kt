package com.vgleadsheets.main

import com.airbnb.mvrx.MvRxState

data class MainActivityState(
    val searchClicked: Boolean = false,
    val searchVisible: Boolean = false,
    val hideSearch: Boolean = false,
    val popBackStack: Boolean = false,
    val searchQuery: String? = null
): MvRxState