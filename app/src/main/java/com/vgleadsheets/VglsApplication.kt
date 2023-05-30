package com.vgleadsheets

import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.airbnb.mvrx.Mavericks
import com.facebook.stetho.Stetho
import com.vgleadsheets.di.AppModule
import com.vgleadsheets.di.DaggerAppComponent
import com.vgleadsheets.images.HatchetCoilLogger
import com.vgleadsheets.images.SheetPreviewFetcher
import com.vgleadsheets.logging.Hatchet
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import javax.inject.Named
import okhttp3.OkHttpClient

class VglsApplication : DaggerApplication(),
    HasAndroidInjector,
    ImageLoaderFactory {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    @Named("VglsOkHttp")
    lateinit var okHttpClient: OkHttpClient

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
    }

    override fun applicationInjector() = DaggerAppComponent
        .factory()
        .create(AppModule(this))

    override fun androidInjector() = dispatchingAndroidInjector

    override fun newImageLoader() = ImageLoader.Builder(this)
        .logger(coilLogger)
        .okHttpClient(okHttpClient)
        .components { add(sheetPreviewFetcherFactory) }
        .build()
}
