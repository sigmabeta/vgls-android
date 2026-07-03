package com.vgleadsheets.nav

import net.sigmabeta.sage.appcomm.SageState

data class NavState(
    val visibility: SystemUiVisibility = SystemUiVisibility.VISIBLE,
) : SageState
