package com.vgleadsheets.images

import androidx.core.graphics.drawable.toDrawable
import coil.ImageLoader
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.Fetcher
import coil.request.Options
import coil.size.pxOrElse
import com.vgleadsheets.bitmaps.LoadingIndicatorGenerator
import javax.inject.Inject

class LoadingIndicatorFetcher(
    private val generator: LoadingIndicatorGenerator,
    private val data: LoadingIndicatorConfig,
    private val options: Options
) : Fetcher {
    override suspend fun fetch() = DrawableResult(
        drawable = generator.generateLoadingSheet(
            options.size.width.pxOrElse { WIDTH_ARBITRARY },
            data.title,
            data.gameName,
            data.composers
        ).toDrawable(options.context.resources),
        isSampled = true,
        dataSource = DataSource.MEMORY
    )

    class Factory @Inject constructor(
        private val generator: LoadingIndicatorGenerator
    ) : Fetcher.Factory<LoadingIndicatorConfig> {
        override fun create(
            data: LoadingIndicatorConfig,
            options: Options,
            imageLoader: ImageLoader
        ) = LoadingIndicatorFetcher(generator, data, options)
    }

    companion object {
        private const val WIDTH_ARBITRARY = 320
    }
}
