package com.vgleadsheets.features.main.feature_name_

import com.airbnb.mvrx.MvRxState
import com.vgleadsheets.args.IdArgs

data class Feature_Name_State(
    val feature_nameId: Long
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}
