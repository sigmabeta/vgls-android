package com.vgleadsheets.mvrx

import com.airbnb.mvrx.MavericksState

interface VglsState : MavericksState {
    fun failure(): Throwable?

    fun isLoading(): Boolean

    fun hasFailed(): Boolean

    fun isEmpty(): Boolean

    fun isReady(): Boolean

    fun isFullyLoaded(): Boolean
}
