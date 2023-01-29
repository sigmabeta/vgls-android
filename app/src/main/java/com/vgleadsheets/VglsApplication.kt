package com.vgleadsheets

import android.graphics.Bitmap
import android.os.Build
import com.airbnb.mvrx.Mavericks
import com.facebook.stetho.Stetho
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.vgleadsheets.di.AppModule
import com.vgleadsheets.di.DaggerAppComponent
import com.vgleadsheets.logging.Hatchet
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class VglsApplication : DaggerApplication(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var okHttp3Downloader: OkHttp3Downloader

    @Inject
    lateinit var hatchet: Hatchet

    override fun onCreate() {
        super.onCreate()

        Mavericks.initialize(this)

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

    override fun applicationInjector() = DaggerAppComponent
        .factory()
        .create(AppModule(this))

    override fun androidInjector() = dispatchingAndroidInjector
}
