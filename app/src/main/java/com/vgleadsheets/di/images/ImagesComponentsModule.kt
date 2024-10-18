package com.vgleadsheets.di.images

import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.vgleadsheets.bitmaps.FakePdfImageGenerator
import com.vgleadsheets.bitmaps.LoadingIndicatorGenerator
import com.vgleadsheets.downloader.FakeSheetDownloader
import com.vgleadsheets.downloader.RealSheetDownloader
import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.images.FakeOtherImageDecoder
import com.vgleadsheets.images.FakeOtherImageFetcher
import com.vgleadsheets.images.HatchetCoilLogger
import com.vgleadsheets.images.LoadingIndicatorFetcher
import com.vgleadsheets.images.LoadingIndicatorKeyer
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.pdf.PdfImageDecoder
import com.vgleadsheets.pdf.PdfImageFetcher
import com.vgleadsheets.pdf.PdfImageKeyer
import com.vgleadsheets.pdf.PdfToBitmapRenderer
import com.vgleadsheets.pdf.fake.FakePdfImageDecoder
import com.vgleadsheets.pdf.fake.FakePdfImageKeyer
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
    internal fun providePdfImageFetcherFactory(sheetDownloader: SheetDownloader) = PdfImageFetcher.Factory(
        sheetDownloader = sheetDownloader
    )

    @Provides
    internal fun provideFakeOtherImageFetcherFactory() = FakeOtherImageFetcher.Factory()

    @Provides
    internal fun provideFakeOtherImageDecoderFactory() = FakeOtherImageDecoder.Factory()

    @Provides
    internal fun provideFakePdfImageKeyer(urlInfoProvider: UrlInfoProvider) = FakePdfImageKeyer(
        urlInfoProvider = urlInfoProvider
    )

    @Provides
    internal fun provideFakePdfImageDecoderFactory(
        generator: FakePdfImageGenerator
    ) = FakePdfImageDecoder.Factory(
        generator = generator,
    )

    @Provides
    internal fun provideSheetDownloader(
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
    internal fun providesRealComponentRegistryBuilderFunction(
        loadingIndicatorKeyer: LoadingIndicatorKeyer,
        loadingIndicatorFetcherFactory: LoadingIndicatorFetcher.Factory,
        pdfImageKeyer: PdfImageKeyer,
        pdfImageFetcherFactory: PdfImageFetcher.Factory,
        pdfImageDecoderFactory: PdfImageDecoder.Factory,
    ): CoilBuilderFunction {
        return CoilBuilderFunction {
            add(loadingIndicatorKeyer)
            add(loadingIndicatorFetcherFactory)
            add(pdfImageFetcherFactory)
            add(pdfImageKeyer)
            add(pdfImageDecoderFactory)
        }
    }

    @Provides
    @Named("FakePdfImageLoaderBuilder")
    internal fun providesFakeComponentRegistryBuilderFunction(
        loadingIndicatorKeyer: LoadingIndicatorKeyer,
        loadingIndicatorFetcherFactory: LoadingIndicatorFetcher.Factory,
        pdfImageFetcherFactory: PdfImageFetcher.Factory,
        fakePdfImageKeyer: FakePdfImageKeyer,
        fakePdfImageDecoderFactory: FakePdfImageDecoder.Factory,
    ): CoilBuilderFunction {
        return CoilBuilderFunction {
            add(loadingIndicatorKeyer)
            add(loadingIndicatorFetcherFactory)
            add(pdfImageFetcherFactory)
            add(fakePdfImageKeyer)
            add(fakePdfImageDecoderFactory)
        }
    }

    @Provides
    @Named("RealOtherImageLoaderBuilder")
    internal fun providesRealOtherBuilderFunction(
        @Named("VglsOkHttp") okHttpClient: OkHttpClient,
    ): CoilBuilderFunction {
        return CoilBuilderFunction {
            add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient }))
        }
    }

    @Provides
    @Named("FakeOtherImageLoaderBuilder")
    internal fun providesFakeOtherBuilderFunction(
        otherImageFetcherFactory: FakeOtherImageFetcher.Factory,
        otherImageDecoderFactory: FakeOtherImageDecoder.Factory,
    ): CoilBuilderFunction {
        return CoilBuilderFunction {
            add(otherImageFetcherFactory)
            add(otherImageDecoderFactory)
        }
    }
}
