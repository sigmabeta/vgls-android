package com.vgleadsheets

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.facebook.stetho.Stetho
import com.vgleadsheets.images.HatchetCoilLogger
import com.vgleadsheets.images.LoadingIndicatorFetcher
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.pdf.PdfImageFetcher
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Named
import okhttp3.OkHttpClient

@HiltAndroidApp
class VglsApplication :
    Application(),
    ImageLoaderFactory {
    @Inject
    @Named("VglsOkHttp")
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var hatchet: Hatchet

    @Inject
    lateinit var coilLogger: HatchetCoilLogger

    @Inject
    lateinit var pdfImageFetcherFactory: PdfImageFetcher.Factory

    @Inject
    lateinit var loadingIndicatorFetcherFactory: LoadingIndicatorFetcher.Factory

    override fun onCreate() {
        super.onCreate()

        hatchet.v("Starting Application.")
        hatchet.v("Build type: ${BuildConfig.BUILD_TYPE}")

        hatchet.v("App version name: ${BuildConfig.VERSION_NAME}")
        hatchet.v("App version code: ${BuildConfig.VERSION_CODE}")

        hatchet.v("Android version: ${Build.VERSION.RELEASE}")
        hatchet.v("Device manufacturer: ${Build.MANUFACTURER}")
        hatchet.v("Device model: ${Build.MODEL}")

        Stetho.initializeWithDefaults(this)
    }

    override fun newImageLoader() = ImageLoader.Builder(this)
        .logger(coilLogger)
        .okHttpClient(okHttpClient)
        .components {
            add(pdfImageFetcherFactory)
            add(loadingIndicatorFetcherFactory)
        }
        .build()
}
