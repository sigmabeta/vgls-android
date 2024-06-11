package com.vgleadsheets.nav

import com.vgleadsheets.appcomm.VglsState

data class NavState(
    val visibility: SystemUiVisibility = SystemUiVisibility.VISIBLE,
) : VglsState
