package com.vgleadsheets.topbar

import com.vgleadsheets.appcomm.VglsState
import com.vgleadsheets.components.TitleBarModel

data class TopBarState(
    val model: TitleBarModel = TitleBarModel(),
    val selectedPart: String? = null,
) : VglsState
