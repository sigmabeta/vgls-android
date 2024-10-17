package com.vgleadsheets.di.images

import coil3.ComponentRegistry
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.vgleadsheets.bitmaps.LoadingIndicatorGenerator
import com.vgleadsheets.downloader.RealSheetDownloader
import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.images.HatchetCoilLogger
import com.vgleadsheets.images.LoadingIndicatorFetcher
import com.vgleadsheets.images.LoadingIndicatorKeyer
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.pdf.PdfImageDecoder
import com.vgleadsheets.pdf.PdfImageFetcher
import com.vgleadsheets.pdf.PdfImageKeyer
import com.vgleadsheets.pdf.PdfToBitmapRenderer
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class ImagesComponentsModule {
    @Provides
    internal fun provideLogger(hatchet: Hatchet) = HatchetCoilLogger(
        hatchet = hatchet,
    )

    @Provides
    internal fun providePdfToBitmapRenderer(hatchet: Hatchet) = PdfToBitmapRenderer(
        hatchet = hatchet,
    )

    @Provides
    internal fun provideLoadingIndicatorKeyer() = LoadingIndicatorKeyer()

    @Provides
    internal fun provideLoadingIndicatorFetcherFactory(
        generator: LoadingIndicatorGenerator
    ) = LoadingIndicatorFetcher.Factory(
        generator = generator,
    )

    @Provides
    internal fun providePdfImageKeyer(urlInfoProvider: UrlInfoProvider) = PdfImageKeyer(
        urlInfoProvider = urlInfoProvider
    )

    @Provides
    internal fun providePdfImageDecoderFactory(pdfToBitmapRenderer: PdfToBitmapRenderer) = PdfImageDecoder.Factory(
        pdfToBitmapRenderer = pdfToBitmapRenderer
    )

    @Provides
    internal fun provideSheetDownloader(
        realSheetDownloader: RealSheetDownloader,
    ): SheetDownloader = realSheetDownloader

    @Provides
    internal fun providePdfImageFetcherFactory(sheetDownloader: RealSheetDownloader) = PdfImageFetcher.Factory(
        sheetDownloader = sheetDownloader
    )

    @Provides
    internal fun providesComponentRegistryBuilderFunction(
        @Named("VglsOkHttp") okHttpClient: OkHttpClient,
        loadingIndicatorKeyer: LoadingIndicatorKeyer,
        loadingIndicatorFetcherFactory: LoadingIndicatorFetcher.Factory,
        pdfImageKeyer: PdfImageKeyer,
        pdfImageFetcherFactory: PdfImageFetcher.Factory,
        pdfImageDecoderFactory: PdfImageDecoder.Factory,
    ): ComponentRegistry.Builder.() -> Unit {
        return {
            add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient }))
            add(loadingIndicatorKeyer)
            add(loadingIndicatorFetcherFactory)
            add(pdfImageFetcherFactory)
            add(pdfImageKeyer)
            add(pdfImageDecoderFactory)
        }
    }
}
