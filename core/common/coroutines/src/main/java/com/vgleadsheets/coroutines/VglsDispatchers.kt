package com.vgleadsheets.coroutines

import kotlinx.coroutines.CoroutineDispatcher

data class VglsDispatchers(
    val computation: CoroutineDispatcher,
    val disk: CoroutineDispatcher,
    val network: CoroutineDispatcher,
    val main: CoroutineDispatcher
)
