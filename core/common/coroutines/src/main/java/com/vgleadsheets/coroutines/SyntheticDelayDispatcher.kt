package com.vgleadsheets.coroutines

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable

class SyntheticDelayDispatcher(
    private val wrapped: CoroutineDispatcher
) : CoroutineDispatcher() {
    @Suppress("MagicNumber")
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        val delayMs: Long = 1000
        println("Injecting delay of $delayMs ms.")

        val wrappedBlock = Runnable {
            Thread.sleep(delayMs)
            block.run()
        }
        wrapped.dispatch(context, wrappedBlock)
    }
}
