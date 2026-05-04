package com.vgleadsheets.images

import coil3.ImageLoader
import coil3.asImage
import coil3.decode.DataSource
import coil3.fetch.Fetcher
import coil3.fetch.ImageFetchResult
import coil3.request.Options
import com.vgleadsheets.bitmaps.LoadingIndicatorGenerator
import javax.inject.Inject

class LoadingIndicatorFetcher(
    private val generator: LoadingIndicatorGenerator,
    private val data: LoadingIndicatorConfig,
) : Fetcher {
    override suspend fun fetch(): ImageFetchResult {
        val maxWidth = requireNotNull(data.maxWidth) { "Loaders must have a width specified." }
        val maxHeight = requireNotNull(data.maxHeight) { "Loaders must have a width specified." }

        return ImageFetchResult(
            image = generator.generateLoadingSheet(
                data.title,
                data.gameName,
                data.composers,
                maxWidth,
                maxHeight,
            ).asImage(),
            isSampled = false,
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
        ) = LoadingIndicatorFetcher(generator, data)
    }
}
