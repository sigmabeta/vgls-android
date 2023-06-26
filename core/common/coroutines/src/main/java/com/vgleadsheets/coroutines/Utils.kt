package com.vgleadsheets.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * From within a suspending fun, join this job or cancel this job if the calling scope gets cancelled first.
 * If the outer scope is cancelled first, this will suspend until the job cancels.
 *
 * This can be used to call execute in a suspending fun that is cancellable like this:
 * suspend {
 *     // Do something
 * }.execute(Dispatchers.IO) { copy(myData = it) }.joinOrCancel()
 */
suspend fun Job.joinOrCancel() {
    try {
        join()
    } catch (e: CancellationException) {
        withContext(NonCancellable) {
            cancelAndJoin()
        }
    }
}
