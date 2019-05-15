package com.vgleadsheets.android

import android.app.Application
import android.os.Build
import timber.log.Timber

class VglsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Timber.v("Starting Application.")
        Timber.v("Build type: %s", BuildConfig.BUILD_TYPE)

        Timber.v("Android version: %s", Build.VERSION.RELEASE)
        Timber.v("Device manufacturer: %s", Build.MANUFACTURER)
        Timber.v("Device model: %s", Build.MODEL)

        // TODO Dagger DI all this stuff
    }
}