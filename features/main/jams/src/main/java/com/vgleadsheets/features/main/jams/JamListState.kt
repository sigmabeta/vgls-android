package com.vgleadsheets.features.main.jams

import com.airbnb.mvrx.MvRxState
import com.vgleadsheets.args.IdArgs

data class JamListState(
    val jamListId: Long
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}
