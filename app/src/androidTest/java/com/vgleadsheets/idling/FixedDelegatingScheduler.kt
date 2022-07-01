package com.vgleadsheets.idling

import androidx.test.espresso.IdlingResource.ResourceCallback
import com.squareup.rx2.idler.IdlingResourceScheduler
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

internal class FixedDelegatingScheduler(
    private val delegate: Scheduler,
    private val name: String
) : IdlingResourceScheduler() {
    private val work = AtomicInteger()
    private var callback: ResourceCallback? = null

    override fun getName(): String {
        return name
    }

    override fun isIdleNow(): Boolean {
        return work.get() == 0
    }

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        this.callback = callback
    }

    override fun createWorker(): Worker {
        val delegateWorker = delegate.createWorker()
        return object : Worker() {
            private val disposables =
                CompositeDisposable(delegateWorker)

            override fun schedule(action: Runnable): Disposable {
                if (disposables.isDisposed) {
                    return Disposables.disposed()
                }
                val work = createWork(action, 0L, 0L)
                val disposable = delegateWorker.schedule(work)
                val workDisposable =
                    ScheduledWorkDisposable(
                        work,
                        disposable
                    )
                disposables.add(workDisposable)
                return workDisposable
            }

            override fun schedule(
                action: Runnable,
                delayTime: Long,
                unit: TimeUnit
            ): Disposable {
                if (disposables.isDisposed) {
                    return Disposables.disposed()
                }
                val work = createWork(action, delayTime, 0L)
                val disposable =
                    delegateWorker.schedule(work, delayTime, unit)
                disposables.add(disposable)
                val workDisposable =
                    ScheduledWorkDisposable(
                        work,
                        disposable
                    )
                disposables.add(workDisposable)
                return workDisposable
            }

            override fun schedulePeriodically(
                action: Runnable,
                initialDelay: Long,
                period: Long,
                unit: TimeUnit
            ): Disposable {
                if (disposables.isDisposed) {
                    return Disposables.disposed()
                }
                val work = createWork(action, initialDelay, period)
                val disposable =
                    delegateWorker.schedulePeriodically(work, initialDelay, period, unit)
                disposables.add(disposable)
                val workDisposable =
                    ScheduledWorkDisposable(
                        work,
                        disposable
                    )
                disposables.add(workDisposable)
                return workDisposable
            }

            override fun dispose() {
                disposables.dispose()
            }

            override fun isDisposed(): Boolean {
                return disposables.isDisposed
            }
        }
    }

    fun startWork() {
        work.incrementAndGet()
    }

    fun stopWork() {
        if (work.decrementAndGet() == 0) {
            callback?.onTransitionToIdle()
        }
    }

    fun createWork(
        action: Runnable,
        delay: Long,
        period: Long
    ): ScheduledWork {
        var localAction = action

        if (localAction is ScheduledWork) { // Unwrap any re-scheduled work. We want each scheduler to get its own state machine.
            localAction = localAction.delegate
        }

        val immediate = delay == 0L
        if (immediate) {
            startWork()
        }

        val startingState = if (immediate) ScheduledWork.STATE_SCHEDULED else ScheduledWork.STATE_IDLE

        return ScheduledWork(
            localAction,
            startingState,
            period > 0L,
            this
        )
    }
}
