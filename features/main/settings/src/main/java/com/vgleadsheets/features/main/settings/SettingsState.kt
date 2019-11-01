package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.storage.Setting

data class SettingsState(
    val settings: Async<List<Setting>> = Uninitialized
) : MvRxState
