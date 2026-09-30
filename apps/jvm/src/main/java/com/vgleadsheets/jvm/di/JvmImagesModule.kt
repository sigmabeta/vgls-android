package com.vgleadsheets.jvm.di

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.pdf.PdfBoxImageDecoder
import com.vgleadsheets.pdf.PdfImageFetcher
import com.vgleadsheets.pdf.PdfImageKeyer
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.di.AppScope
import io.ktor.client.HttpClient
import net.sigmabeta.sage.logging.Hatchet
import okio.Path.Companion.toOkioPath
import java.io.File

/**
 * Desktop mirror of apps/android's ImagesComponentsModule + ImagesModule: the PDF (sheet) pipeline
 * (the shared commonMain [PdfImageKeyer]/[PdfImageFetcher] plus the JVM-only [PdfBoxImageDecoder]),
 * and a network fetcher on the shared ktor [HttpClient] for game cover art, cached on disk under the
 * work dir. Unlike android there's no telephoto viewer path. It's registered as the singleton loader
 * in DesktopMain (via `graph.imageLoader`), so `AsyncImage`/`rememberAsyncImagePainter` in the shared
 * UI resolve it.
 */
@BindingContainer
@ContributesTo(AppScope::class)
object JvmImagesModule {
    @Provides
    fun providePdfImageKeyer(urlInfoProvider: UrlInfoProvider): PdfImageKeyer = PdfImageKeyer(
        urlInfoProvider = urlInfoProvider,
    )

    @Provides
    fun providePdfImageFetcherFactory(
        sheetDownloader: SheetDownloader,
    ): PdfImageFetcher.Factory = PdfImageFetcher.Factory(
        sheetDownloader = sheetDownloader,
    )

    @Provides
    fun providePdfImageDecoderFactory(hatchet: Hatchet): PdfBoxImageDecoder.Factory = PdfBoxImageDecoder.Factory(
        hatchet = hatchet,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideImageLoader(
        pdfImageKeyer: PdfImageKeyer,
        pdfImageFetcherFactory: PdfImageFetcher.Factory,
        pdfImageDecoderFactory: PdfBoxImageDecoder.Factory,
        httpClient: HttpClient,
        @Named("workDir") workDir: File,
    ): ImageLoader = ImageLoader.Builder(PlatformContext.INSTANCE)
        .components {
            add(pdfImageKeyer)
            add(pdfImageFetcherFactory)
            add(pdfImageDecoderFactory)
            add(KtorNetworkFetcherFactory(httpClient))
        }
        .diskCache {
            DiskCache.Builder()
                .directory(File(workDir, "image-cache").toOkioPath())
                .maxSizeBytes(IMAGE_CACHE_BYTES)
                .build()
        }
        .build()

    private const val IMAGE_CACHE_BYTES = 128L * 1024 * 1024
}
