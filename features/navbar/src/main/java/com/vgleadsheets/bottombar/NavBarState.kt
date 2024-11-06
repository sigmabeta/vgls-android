package com.vgleadsheets.bottombar

import com.vgleadsheets.appcomm.VglsState

data class NavBarState(
    val visibility: NavBarVisibility = NavBarVisibility.VISIBLE
) : VglsState
