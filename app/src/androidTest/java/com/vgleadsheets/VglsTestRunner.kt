package com.vgleadsheets

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.vgleadsheets.idling.FixedRxIdler
import io.reactivex.plugins.RxJavaPlugins


class VglsTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        loader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(
            loader,
            UiTestApplication::class.java.name,
            context
        )
    }

    override fun onStart() {
        RxJavaPlugins.setInitComputationSchedulerHandler(
            FixedRxIdler.create("RxJava 2.x Computation Scheduler")
        )
        RxJavaPlugins.setInitIoSchedulerHandler(
            FixedRxIdler.create("RxJava 2.x IO Scheduler")
        )

        super.onStart()
    }
}
