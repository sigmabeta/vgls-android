package com.vgleadsheets

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner


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

    @Suppress("DEPRECATION")
    override fun onStart() {
//        RxJavaPlugins.setInitComputationSchedulerHandler(
//            Rx2Idler.create("RxJava 2.x Computation Scheduler")
//        )
//        RxJavaPlugins.setInitIoSchedulerHandler(
//            Rx2Idler.create("RxJava 2.x IO Scheduler")
//        )

        super.onStart()
    }
}
