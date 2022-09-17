package com.vgleadsheets.coroutines

import kotlinx.coroutines.CoroutineDispatcher

data class VglsDispatchers(
    val computation: CoroutineDispatcher,
    val database: CoroutineDispatcher,
    val disk: CoroutineDispatcher,
    val network: CoroutineDispatcher,
    val main: CoroutineDispatcher
)
