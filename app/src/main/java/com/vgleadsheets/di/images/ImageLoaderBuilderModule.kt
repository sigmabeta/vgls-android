package com.vgleadsheets.di.images

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderBuilderModule {
    @Provides
    @Named("PdfImageLoaderBuilder")
    internal fun providesComponentRegistryBuilderFunction(
        @Named("VglsPdfUrl") baseUrl: String?,
        @Named("FakePdfImageLoaderBuilder") fake: CoilBuilderFunction,
        @Named("RealPdfImageLoaderBuilder") real: CoilBuilderFunction,
    ): CoilBuilderFunction = if (baseUrl != null) {
        real
    } else {
        fake
    }

    @Provides
    @Named("OtherImageLoaderBuilder")
    internal fun providesOtherBuilderFunction(
        @Named("VglsImageUrl") baseUrl: String?,
        @Named("FakeOtherImageLoaderBuilder") fake: CoilBuilderFunction,
        @Named("RealOtherImageLoaderBuilder") real: CoilBuilderFunction,
    ): CoilBuilderFunction = if (baseUrl != null) {
        real
    } else {
        fake
    }
}
