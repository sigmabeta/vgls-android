package com.vgleadsheets.features.main.debug

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.CompositeState

data class DebugState(
    val changed: Boolean = false,
    val sheetDeletion: Async<Unit> = Uninitialized,
    val jamDeletion: Async<Unit> = Uninitialized,
    override val contentLoad: DebugContent = DebugContent(
        Uninitialized
    ),
) : CompositeState<DebugContent>
