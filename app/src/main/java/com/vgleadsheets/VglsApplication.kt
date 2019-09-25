package com.vgleadsheets

import android.app.Application
import android.os.Build
import com.facebook.stetho.Stetho
import com.vgleadsheets.di.AppModule
import com.vgleadsheets.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class VglsApplication : Application(), HasAndroidInjector {
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

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

        Stetho.initializeWithDefaults(this)
    }

    override fun androidInjector() = dispatchingAndroidInjector
}
