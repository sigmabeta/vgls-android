package com.vgleadsheets.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

object CustomFlows {
    fun <Emission> emitOnInterval(intervalMillis: Long, emitter: suspend () -> Emission) = flow {
        while (true) {
            delay(intervalMillis)
            emit(emitter())
        }
    }
}
