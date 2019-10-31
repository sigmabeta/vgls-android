package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized

data class SettingsState(
    val settings: Async<List<Boolean>> = Uninitialized
) : MvRxState
