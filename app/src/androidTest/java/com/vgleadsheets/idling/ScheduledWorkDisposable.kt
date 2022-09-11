package com.vgleadsheets.idling

import kotlinx.coroutines.Job

internal class ScheduledWorkJob(
    private val work: ScheduledWork,
    private val delegate: Job
) :
    Job {
    override fun dispose() {
        work.dispose()
        delegate.dispose()
    }

    override fun isDisposed(): Boolean {
        return work.get() == ScheduledWork.Companion.STATE_DISPOSED
    }
}
