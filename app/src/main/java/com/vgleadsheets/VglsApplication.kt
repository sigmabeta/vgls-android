package com.vgleadsheets

import android.graphics.Bitmap
import android.os.Build
import com.facebook.stetho.Stetho
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.vgleadsheets.di.AppModule
import com.vgleadsheets.di.DaggerAppComponent
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import timber.log.Timber

class VglsApplication : DaggerApplication(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var okHttp3Downloader: OkHttp3Downloader

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Timber.v("Starting Application.")
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

    override fun applicationInjector() = DaggerAppComponent
        .factory()
        .create(AppModule(this))

    override fun androidInjector() = dispatchingAndroidInjector
}
