package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.LCE

data class Pairing(
    val module: HomeModule,
    val state: LCE<HomeModuleState>,
)
