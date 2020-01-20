package com.vgleadsheets.features.main.debug

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.storage.Setting

data class DebugState(
    val settings: Async<List<Setting>> = Uninitialized,
    val sheetDeletion: Async<Unit> = Uninitialized,
    val jamDeletion: Async<Unit> = Uninitialized,
    val changed: Boolean = false
) : MvRxState
