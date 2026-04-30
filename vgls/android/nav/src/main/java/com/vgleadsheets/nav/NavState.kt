package com.vgleadsheets.nav

import net.sigmabeta.sage.appcomm.VglsState

data class NavState(
    val visibility: SystemUiVisibility = SystemUiVisibility.VISIBLE,
) : VglsState
