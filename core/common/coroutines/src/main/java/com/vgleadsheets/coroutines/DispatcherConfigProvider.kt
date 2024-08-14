package com.vgleadsheets.coroutines

interface DispatcherConfigProvider {
    fun shouldUseDelayDispatcher(): Boolean
}
