package com.vgleadsheets.topbar

import com.vgleadsheets.state.VglsState

data class TopBarState(
    val title: String? = null,
    val subtitle: String? = null,
): VglsState
