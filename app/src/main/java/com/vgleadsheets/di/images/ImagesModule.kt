package com.vgleadsheets.di.images

import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.vgleadsheets.images.HatchetCoilLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class ImagesModule {
    @Provides
    @JvmSuppressWildcards
    internal fun provideImageLoader(
        @ApplicationContext context: Context,
        coilLogger: HatchetCoilLogger,
        @Named("PdfImageLoaderBuilder") pdfBuilder: CoilBuilderFunction,
        @Named("OtherImageLoaderBuilder") otherBuilder: CoilBuilderFunction,
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .logger(coilLogger)
            .components {
                pdfBuilder.function(this)
                otherBuilder.function(this)
            }
            .build()
            .also { loader -> SingletonImageLoader.setSafe { loader } }
    }
}
