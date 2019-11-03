package com.vgleadsheets.features.main.about

import com.airbnb.mvrx.MvRxState
import com.vgleadsheets.args.IdArgs

data class AboutState(
    val aboutId: Long
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}
