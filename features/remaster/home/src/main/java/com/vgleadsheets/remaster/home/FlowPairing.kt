package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.LCE
import kotlinx.coroutines.flow.StateFlow

data class FlowPairing(
    val module: HomeModule,
    val flow: StateFlow<LCE<HomeModuleState>>,
)
