package com.vgleadsheets

import android.graphics.Bitmap
import android.os.Build
import com.facebook.stetho.Stetho
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.vgleadsheets.di.DaggerUiTestAppComponent
import com.vgleadsheets.di.TestAppModule
import com.vgleadsheets.di.UiTestAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class UiTestApplication : DaggerApplication(), HasAndroidInjector {
    lateinit var testComponent: UiTestAppComponent

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var okHttp3Downloader: OkHttp3Downloader

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Timber.v("Starting Test Application.")
        Timber.v("Build type: %s", BuildConfig.BUILD_TYPE)

        Timber.v("Android version: %s", Build.VERSION.RELEASE)
        Timber.v("Device manufacturer: %s", Build.MANUFACTURER)
        Timber.v("Device model: %s", Build.MODEL)

        Stetho.initializeWithDefaults(this)

        val picasso = Picasso.Builder(this)
            .downloader(okHttp3Downloader)
            .indicatorsEnabled(BuildConfig.DEBUG)
            .defaultBitmapConfig(Bitmap.Config.RGB_565)
            .build()

        Picasso.setSingletonInstance(picasso)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        val component = DaggerUiTestAppComponent
            .factory()
            .create(TestAppModule(this))

        testComponent = component
        return component
    }

    override fun androidInjector() = dispatchingAndroidInjector
}
