package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.MvRxState
import com.vgleadsheets.args.IdArgs

data class HudState(
    val hudId: Long
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}
