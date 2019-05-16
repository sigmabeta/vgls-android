package com.vgleadsheets.android

import android.app.Activity
import android.app.Application
import android.os.Build
import com.vgleadsheets.android.di.DaggerAppComponent
import dagger.android.HasActivityInjector
import timber.log.Timber
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class VglsApplication : Application(), HasActivityInjector {
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
        @Inject set

    override fun onCreate() {
        super.onCreate()

        val appComponent = DaggerAppComponent.create()

        appComponent.inject(this)

        Timber.plant(Timber.DebugTree())
        Timber.v("Starting Application.")
        Timber.v("Build type: %s", BuildConfig.BUILD_TYPE)

        Timber.v("Android version: %s", Build.VERSION.RELEASE)
        Timber.v("Device manufacturer: %s", Build.MANUFACTURER)
        Timber.v("Device model: %s", Build.MODEL)
    }

    override fun activityInjector() = dispatchingActivityInjector
}