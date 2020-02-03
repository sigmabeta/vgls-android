package com.vgleadsheets.idling

import io.reactivex.disposables.Disposable

internal class ScheduledWorkDisposable(
    private val work: ScheduledWork,
    private val delegate: Disposable
) :
    Disposable {
    override fun dispose() {
        work.dispose()
        delegate.dispose()
    }

    override fun isDisposed(): Boolean {
        return work.get() == ScheduledWork.Companion.STATE_DISPOSED
    }

}
