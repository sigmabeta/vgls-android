package com.vgleadsheets.coroutines

import kotlinx.coroutines.CoroutineDispatcher

data class VglsDispatchers(
    val computation: CoroutineDispatcher,
    val disk: CoroutineDispatcher,
    val network: CoroutineDispatcher,
    val main: CoroutineDispatcher
) {
    companion object {
        private const val DEP_NAME_PREFIX = "Dep.Dispatcher"

        const val DEP_NAME_DISPATCHER_DELAY = "$DEP_NAME_PREFIX.Delay"
        const val DEP_NAME_DISPATCHER_REGULAR = "$DEP_NAME_PREFIX.Regular"
    }
}
