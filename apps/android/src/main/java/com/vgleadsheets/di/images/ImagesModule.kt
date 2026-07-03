package com.vgleadsheets.di.images

import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.vgleadsheets.images.HatchetCoilLogger
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import net.sigmabeta.sage.di.AppScope

@BindingContainer
@ContributesTo(AppScope::class)
object ImagesModule {
    @Provides
    @JvmSuppressWildcards
    fun provideImageLoader(
        context: Context,
        coilLogger: HatchetCoilLogger,
        @Named("PdfImageLoaderBuilder") pdfBuilder: CoilBuilderFunction,
        @Named("OtherImageLoaderBuilder") otherBuilder: CoilBuilderFunction,
    ): ImageLoader {
        val builder = ImageLoader.Builder(context)

        return with(builder) {
            logger(coilLogger)

            components {
                pdfBuilder.function(this)
                otherBuilder.function(this)
            }

            build()
        }.also { loader -> SingletonImageLoader.setSafe { loader } }
    }
}
