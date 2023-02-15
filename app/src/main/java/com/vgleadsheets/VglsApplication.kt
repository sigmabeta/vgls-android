package com.vgleadsheets

import android.graphics.Bitmap
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.airbnb.mvrx.Mavericks
import com.facebook.stetho.Stetho
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.vgleadsheets.di.AppModule
import com.vgleadsheets.di.DaggerAppComponent
import com.vgleadsheets.images.HatchetCoilLogger
import com.vgleadsheets.images.SheetPreviewFetcher
import com.vgleadsheets.logging.Hatchet
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Named

class VglsApplication : DaggerApplication(),
    HasAndroidInjector,
    ImageLoaderFactory {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    @Named("VglsOkHttp")
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var okHttp3Downloader: OkHttp3Downloader

    @Inject
    lateinit var hatchet: Hatchet

    @Inject
    lateinit var coilLogger: HatchetCoilLogger

    @Inject
    lateinit var sheetPreviewFetcherFactory: SheetPreviewFetcher.Factory

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
            // .downloader(okHttp3Downloader)
            .indicatorsEnabled(BuildConfig.DEBUG)
            .defaultBitmapConfig(Bitmap.Config.RGB_565)
            .build()

        Picasso.setSingletonInstance(picasso)
    }

    override fun applicationInjector() = DaggerAppComponent
        .factory()
        .create(AppModule(this))

    override fun androidInjector() = dispatchingAndroidInjector

    override fun newImageLoader() = ImageLoader.Builder(this)
        .logger(coilLogger)
        .okHttpClient(okHttpClient)
        .components {
            add(sheetPreviewFetcherFactory)
        }
        .build()
}
