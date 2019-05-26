package com.vgleadsheets.android

import android.app.Activity
import android.app.Application
import android.os.Build
import com.vgleadsheets.android.di.AppModule
import com.vgleadsheets.android.di.DaggerAppComponent
import com.vgleadsheets.database.di.DatabaseModule
import com.vgleadsheets.network.di.NetworkModule
import com.vgleadsheets.repository.di.RepositoryModule
import dagger.android.HasActivityInjector
import timber.log.Timber
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class VglsApplication : Application(), HasActivityInjector {
    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        val appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

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