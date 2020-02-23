package com.vgleadsheets.idling

import androidx.test.espresso.IdlingRegistry
import com.squareup.rx2.idler.IdlingResourceScheduler
import io.reactivex.Scheduler
import io.reactivex.functions.Function
import java.util.concurrent.Callable

class FixedRxIdler {
    companion object {
        fun create(name: String): Function<Callable<Scheduler>, Scheduler>? {
            return Function { delegate ->
                val scheduler: IdlingResourceScheduler =
                    FixedDelegatingScheduler(delegate.call(), name)
                IdlingRegistry.getInstance().register(scheduler)

                scheduler
            }
        }
    }
}
