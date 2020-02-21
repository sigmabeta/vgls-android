package com.vgleadsheets.idling

import java.util.concurrent.atomic.AtomicInteger

internal class ScheduledWork(
    val delegate: Runnable,
    startingState: Int,
    private val isPeriodic: Boolean,
    private val scheduler: FixedDelegatingScheduler
) : AtomicInteger(startingState), Runnable {
    override fun run() {
        while (true) {
            when (val state = get()) {
                STATE_IDLE, STATE_SCHEDULED -> {
                    val someValue = compareAndSet(state, STATE_RUNNING)
                    if (someValue) {
                        if (state == STATE_IDLE) {
                            scheduler.startWork()
                        }

                        try {
                            delegate.run()
                        } finally {
                            // Change state with a CAS to ensure we don't overwrite a disposed state.
                            compareAndSet(
                                STATE_RUNNING,
                                if (isPeriodic) STATE_IDLE else STATE_COMPLETED
                            )

                            scheduler.stopWork()
                        }

                        return // CAS success, we're done.
                    }
                }
                STATE_RUNNING -> throw IllegalStateException("Already running")
                STATE_COMPLETED -> throw IllegalStateException("Already completed")
                STATE_DISPOSED -> return // Nothing to do.
            }
        }
    }

    fun dispose() {
        while (true) {
            val state = get()
            if (state == STATE_DISPOSED) {
                return // Nothing to do.
            } else if (compareAndSet(state, STATE_DISPOSED)
            ) {
                // If idle, startWork() hasn't been called so we don't need a matching stopWork(). 
                // If running, startWork() was called but the try/finally ensures a stopWork() call. 
                // If completed, both startWork() and stopWork() have been called.
                if (state == STATE_SCHEDULED) {
                    scheduler.stopWork() // Scheduled but not running means we called startWork().
                }
                return
            }
        }
    }

    override fun toByte() = get().toByte()

    override fun toChar() = get().toChar()

    override fun toShort() = get().toShort()

    companion object {
        const val STATE_IDLE = 0 // --> STATE_RUNNING, STATE_DISPOSED
        const val STATE_SCHEDULED = 1 // --> STATE_RUNNING, STATE_DISPOSED
        const val STATE_RUNNING = 2 // --> STATE_IDLE, STATE_COMPLETED, STATE_DISPOSED
        const val STATE_COMPLETED = 3 // --> STATE_DISPOSED
        const val STATE_DISPOSED = 4
    }
}
