package com.vgleadsheets.topbar

import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.state.VglsState

data class TopBarState(
    val model: TitleBarModel = TitleBarModel(),
    val selectedPart: String? = null,
) : VglsState
