package com.vgleadsheets.di.images

import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import net.sigmabeta.sage.di.AppScope

@BindingContainer
@ContributesTo(AppScope::class)
object ImageLoaderBuiFlderModule {
    @Provides
    @Named("PdfImageLoaderBuilder")
    fun providesComponentRegistryBuilderFunction(
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
    fun providesOtherBuilderFunction(
        @Named("VglsImageUrl") baseUrl: String?,
        @Named("FakeOtherImageLoaderBuilder") fake: CoilBuilderFunction,
        @Named("RealOtherImageLoaderBuilder") real: CoilBuilderFunction,
    ): CoilBuilderFunction = if (baseUrl != null) {
        real
    } else {
        fake
    }
}
