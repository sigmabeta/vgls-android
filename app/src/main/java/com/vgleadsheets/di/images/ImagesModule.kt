package com.vgleadsheets.di.images

import android.content.Context
import coil3.ComponentRegistry
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.vgleadsheets.images.HatchetCoilLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ImagesModule {
    @Provides
    @JvmSuppressWildcards
    internal fun provideImageLoader(
        @ApplicationContext context: Context,
        coilLogger: HatchetCoilLogger,
        builder: ComponentRegistry.Builder.() -> Unit,
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .logger(coilLogger)
            .components(builder)
            .build()
            .also { loader -> SingletonImageLoader.setSafe { loader } }
    }
}
