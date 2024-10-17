package com.vgleadsheets.images

import coil3.ImageLoader
import coil3.asImage
import coil3.decode.DataSource
import coil3.fetch.Fetcher
import coil3.fetch.ImageFetchResult
import coil3.request.Options
import coil3.size.pxOrElse
import com.vgleadsheets.bitmaps.LoadingIndicatorGenerator
import javax.inject.Inject

class LoadingIndicatorFetcher(
    private val generator: LoadingIndicatorGenerator,
    private val data: LoadingIndicatorConfig,
    private val options: Options
) : Fetcher {
    override suspend fun fetch(): ImageFetchResult {
        return ImageFetchResult(
            image = generator.generateLoadingSheet(
                options.size.width.pxOrElse { WIDTH_ARBITRARY },
                data.title,
                data.gameName,
                data.composers
            ).asImage(),
            isSampled = true,
            dataSource = DataSource.MEMORY
        )
    }

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
