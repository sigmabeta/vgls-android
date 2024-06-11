package com.vgleadsheets.bottombar

import com.vgleadsheets.appcomm.VglsState

data class BottomBarState(
    val visibility: BottomBarVisibility = BottomBarVisibility.VISIBLE
) : VglsState
