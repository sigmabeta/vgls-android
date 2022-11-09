package com.vgleadsheets.mvrx

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel

@Suppress("UnnecessaryAbstractClass")
abstract class MavericksViewModel<S : MavericksState>(initialState: S) :
    MavericksViewModel<S>(initialState)
