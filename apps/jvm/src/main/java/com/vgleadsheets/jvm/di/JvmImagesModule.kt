package com.vgleadsheets.jvm.di

import coil3.ImageLoader
import coil3.PlatformContext
import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.pdf.PdfBoxImageDecoder
import com.vgleadsheets.pdf.PdfImageFetcher
import com.vgleadsheets.pdf.PdfImageKeyer
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet

/**
 * Desktop mirror of apps/android's ImagesComponentsModule + ImagesModule, pared down to the PDF
 * (sheet) pipeline: the shared commonMain [PdfImageKeyer]/[PdfImageFetcher] plus the JVM-only
 * [PdfBoxImageDecoder]. Unlike android there's no telephoto viewer path and no network-image
 * ("Other") fetcher — desktop only needs sheet pages, which the non-zooming viewer and the list
 * thumbnails both request through this single Coil [ImageLoader]. It's registered as the singleton
 * loader in DesktopMain (via `graph.imageLoader`), so `AsyncImage`/`rememberAsyncImagePainter` in the
 * shared UI resolve it.
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
    ): ImageLoader = ImageLoader.Builder(PlatformContext.INSTANCE)
        .components {
            add(pdfImageKeyer)
            add(pdfImageFetcherFactory)
            add(pdfImageDecoderFactory)
        }
        .build()
}
