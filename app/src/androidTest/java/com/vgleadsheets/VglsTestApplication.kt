package com.vgleadsheets

import android.app.Application
import android.os.Build
import coil3.ImageLoader
import coil3.ImageLoaderFactory
import com.vgleadsheets.images.HatchetCoilLogger
import com.vgleadsheets.images.LoadingIndicatorFetcher
import com.vgleadsheets.images.LoadingIndicatorKeyer
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.pdf.PdfImageDecoder
import com.vgleadsheets.pdf.PdfImageFetcher
import com.vgleadsheets.pdf.PdfImageKeyer
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.repository.history.UserContentMigrator
import com.vgleadsheets.time.ThreeTenTime
import com.vgleadsheets.versions.AppVersionManager
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Named

class VglsTestApplication :
    Application(),
    ImageLoaderFactory {
    @Inject
    lateinit var updateManager: UpdateManager

    @Inject
    @Named("VglsOkHttp")
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var hatchet: Hatchet

    @Inject
    lateinit var userContentMigrator: UserContentMigrator

    @Inject
    lateinit var appVersionManager: AppVersionManager

    @Inject
    lateinit var threeTenTime: ThreeTenTime

    @Inject
    lateinit var coilLogger: HatchetCoilLogger

    @Inject
    lateinit var loadingIndicatorKeyer: LoadingIndicatorKeyer

    @Inject
    lateinit var pdfImageKeyer: PdfImageKeyer

    @Inject
    lateinit var pdfImageDecoderFactory: PdfImageDecoder.Factory

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

        threeTenTime.init()
        appVersionManager.reportAppVersion(BuildConfig.VERSION_CODE)
    }

    override fun newImageLoader() = ImageLoader.Builder(this)
        .logger(coilLogger)
        .okHttpClient(okHttpClient)
        .respectCacheHeaders(false)
        .components {
            add(loadingIndicatorKeyer)
            add(pdfImageKeyer)
            add(pdfImageDecoderFactory)
            add(pdfImageFetcherFactory)
            add(loadingIndicatorFetcherFactory)
        }
        .build()
}
