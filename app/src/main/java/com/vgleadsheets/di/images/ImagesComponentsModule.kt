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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.sigmabeta.sage.logging.Hatchet
import okhttp3.OkHttpClient
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class ImagesComponentsModule {
    @Provides
    fun provideLoadingIndicatorKeyer() = LoadingIndicatorKeyer()

    @Provides
    fun provideLoadingIndicatorFetcherFactory(
        generator: LoadingIndicatorGenerator
    ) = LoadingIndicatorFetcher.Factory(
        generator = generator,
    )

    @Provides
    fun providePdfImageKeyer(urlInfoProvider: UrlInfoProvider) = PdfImageKeyer(
        urlInfoProvider = urlInfoProvider
    )

    @Provides
    fun providePdfImageDecoderFactory(hatchet: Hatchet) = PdfImageDecoder.Factory(
        hatchet = hatchet,
    )

    @Provides
    fun providePdfImageFetcherFactory(sheetDownloader: SheetDownloader) = PdfImageFetcher.Factory(
        sheetDownloader = sheetDownloader
    )

    @Provides
    fun provideFakeOtherImageFetcherFactory() = FakeOtherImageFetcher.Factory()

    @Provides
    fun provideFakeOtherImageDecoderFactory() = FakeOtherImageDecoder.Factory()

    @Provides
    fun provideFakePdfImageKeyer(urlInfoProvider: UrlInfoProvider) = FakePdfImageKeyer(
        urlInfoProvider = urlInfoProvider
    )

    @Provides
    fun provideFakePdfImageDecoderFactory(
        generator: FakePdfImageGenerator
    ) = FakePdfImageDecoder.Factory(
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
