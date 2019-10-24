package com.vgleadsheets

import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import com.facebook.stetho.Stetho
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.vgleadsheets.di.AppModule
import com.vgleadsheets.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class VglsApplication : Application(), HasAndroidInjector {
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject lateinit var okHttp3Downloader: OkHttp3Downloader

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

        val picasso = Picasso.Builder(this)
            .downloader(okHttp3Downloader)
            .defaultBitmapConfig(Bitmap.Config.RGB_565)
            .build()

        Picasso.setSingletonInstance(picasso)
    }

    override fun androidInjector() = dispatchingAndroidInjector
}
