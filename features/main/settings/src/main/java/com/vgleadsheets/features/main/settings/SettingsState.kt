package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.MvRxState
import com.vgleadsheets.args.IdArgs

data class SettingsState(
    val settingsId: Long
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}
