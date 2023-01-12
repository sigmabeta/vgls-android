package com.vgleadsheets

import android.graphics.Bitmap
import android.os.Build
import com.facebook.stetho.Stetho
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.vgleadsheets.di.DaggerUiTestAppComponent
import com.vgleadsheets.di.TestAppModule
import com.vgleadsheets.di.UiTestAppComponent
import com.vgleadsheets.logging.Hatchet
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class UiTestApplication : DaggerApplication(), HasAndroidInjector {
    lateinit var testComponent: UiTestAppComponent

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var okHttp3Downloader: OkHttp3Downloader

    @Inject
    lateinit var hatchet: Hatchet

    override fun onCreate() {
        super.onCreate()

        hatchet.v(this.javaClass.simpleName, "Starting Application.")
        hatchet.v(this.javaClass.simpleName, "Build type: ${BuildConfig.BUILD_TYPE}")

        hatchet.v(this.javaClass.simpleName, "Android version: ${Build.VERSION.RELEASE}")
        hatchet.v(this.javaClass.simpleName, "Device manufacturer: ${Build.MANUFACTURER}")
        hatchet.v(this.javaClass.simpleName, "Device model: ${Build.MODEL}")
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
