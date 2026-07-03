package com.vgleadsheets.di.images

import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.vgleadsheets.bitmaps.FakePdfImageGenerator
import com.vgleadsheets.bitmaps.LoadingIndicatorGenerator
import com.vgleadsheets.downloader.FakeSheetDownloader
import com.vgleadsheets.downloader.RealSheetDownloader
import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.images.FakeOtherImageDecoder
import com.vgleadsheets.images.FakeOtherImageFetcher
import com.vgleadsheets.images.LoadingIndicatorFetcher
import com.vgleadsheets.images.LoadingIndicatorKeyer
import com.vgleadsheets.pdf.PdfImageDecoder
import com.vgleadsheets.pdf.PdfImageFetcher
import com.vgleadsheets.pdf.PdfImageKeyer
import com.vgleadsheets.pdf.fake.FakePdfImageDecoder
import com.vgleadsheets.pdf.fake.FakePdfImageKeyer
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import okhttp3.OkHttpClient

@BindingContainer
@ContributesTo(AppScope::class)
object ImagesComponentsModule {
    @Provides
    fun provideLoadingIndicatorKeyer(): LoadingIndicatorKeyer = LoadingIndicatorKeyer()

    @Provides
    fun provideLoadingIndicatorFetcherFactory(
        generator: LoadingIndicatorGenerator
    ): LoadingIndicatorFetcher.Factory = LoadingIndicatorFetcher.Factory(
        generator = generator,
    )

    @Provides
    fun providePdfImageKeyer(urlInfoProvider: UrlInfoProvider): PdfImageKeyer = PdfImageKeyer(
        urlInfoProvider = urlInfoProvider
    )

    @Provides
    fun providePdfImageDecoderFactory(hatchet: Hatchet): PdfImageDecoder.Factory = PdfImageDecoder.Factory(
        hatchet = hatchet,
    )

    @Provides
    fun providePdfImageFetcherFactory(
        sheetDownloader: SheetDownloader,
    ): PdfImageFetcher.Factory = PdfImageFetcher.Factory(
        sheetDownloader = sheetDownloader
    )

    @Provides
    fun provideFakeOtherImageFetcherFactory(): FakeOtherImageFetcher.Factory = FakeOtherImageFetcher.Factory()

    @Provides
    fun provideFakeOtherImageDecoderFactory(): FakeOtherImageDecoder.Factory = FakeOtherImageDecoder.Factory()

    @Provides
    fun provideFakePdfImageKeyer(urlInfoProvider: UrlInfoProvider): FakePdfImageKeyer = FakePdfImageKeyer(
        urlInfoProvider = urlInfoProvider
    )

    @Provides
    fun provideFakePdfImageDecoderFactory(
        generator: FakePdfImageGenerator
    ): FakePdfImageDecoder.Factory = FakePdfImageDecoder.Factory(
        generator = generator,
    )

    @Provides
    fun provideSheetDownloader(
        @Named("VglsPdfUrl") baseUrl: String?,
        fakeSheetDownloader: FakeSheetDownloader,
        realSheetDownloader: RealSheetDownloader,
    ): SheetDownloader = if (baseUrl != null) {
        realSheetDownloader
    } else {
        fakeSheetDownloader
    }

    @Provides
    @Named("RealPdfImageLoaderBuilder")
    fun providesRealComponentRegistryBuilderFunction(
        loadingIndicatorKeyer: LoadingIndicatorKeyer,
        loadingIndicatorFetcherFactory: LoadingIndicatorFetcher.Factory,
        pdfImageKeyer: PdfImageKeyer,
        pdfImageFetcherFactory: PdfImageFetcher.Factory,
        pdfImageDecoderFactory: PdfImageDecoder.Factory,
    ): CoilBuilderFunction = CoilBuilderFunction {
            add(loadingIndicatorKeyer)
            add(loadingIndicatorFetcherFactory)
            add(pdfImageFetcherFactory)
            add(pdfImageKeyer)
            add(pdfImageDecoderFactory)
        }

    @Provides
    @Named("FakePdfImageLoaderBuilder")
    fun providesFakeComponentRegistryBuilderFunction(
        loadingIndicatorKeyer: LoadingIndicatorKeyer,
        loadingIndicatorFetcherFactory: LoadingIndicatorFetcher.Factory,
        pdfImageFetcherFactory: PdfImageFetcher.Factory,
        fakePdfImageKeyer: FakePdfImageKeyer,
        fakePdfImageDecoderFactory: FakePdfImageDecoder.Factory,
    ): CoilBuilderFunction = CoilBuilderFunction {
            add(loadingIndicatorKeyer)
            add(loadingIndicatorFetcherFactory)
            add(pdfImageFetcherFactory)
            add(fakePdfImageKeyer)
            add(fakePdfImageDecoderFactory)
        }

    @Provides
    @Named("RealOtherImageLoaderBuilder")
    fun providesRealOtherBuilderFunction(
        @Named("VglsOkHttp") okHttpClient: OkHttpClient,
    ): CoilBuilderFunction = CoilBuilderFunction {
        add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient }))
    }

    @Provides
    @Named("FakeOtherImageLoaderBuilder")
    fun providesFakeOtherBuilderFunction(
        otherImageFetcherFactory: FakeOtherImageFetcher.Factory,
        otherImageDecoderFactory: FakeOtherImageDecoder.Factory,
    ): CoilBuilderFunction = CoilBuilderFunction {
            add(otherImageFetcherFactory)
            add(otherImageDecoderFactory)
        }
}
