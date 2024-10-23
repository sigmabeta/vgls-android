package com.vgleadsheets.di.network

import com.vgleadsheets.di.images.CoilBuilderFunction
import com.vgleadsheets.di.images.ImageLoaderBuilderModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ImageLoaderBuilderModule::class]
)
object FakeImageLoaderBuilderModule {
    @Provides
    @Named("PdfImageLoaderBuilder")
    internal fun providesComponentRegistryBuilderFunction(
        @Named("FakePdfImageLoaderBuilder") fake: CoilBuilderFunction,
    ): CoilBuilderFunction = fake

    @Provides
    @Named("OtherImageLoaderBuilder")
    internal fun providesOtherBuilderFunction(
        @Named("FakeOtherImageLoaderBuilder") fake: CoilBuilderFunction,
    ): CoilBuilderFunction = fake
}
