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
    private val data: PagePreview,
    private val options: Options
) : Fetcher {
    override suspend fun fetch() = DrawableResult(
        drawable = generator.generateLoadingSheet(
            options.size.width.pxOrElse { 320 },
            data.title,
            data.transposition,
            data.gameName,
            data.composers
        ).toDrawable(options.context.resources),
        isSampled = true,
        dataSource = DataSource.MEMORY
    )

    class Factory @Inject constructor(
        private val generator: LoadingIndicatorGenerator
    ) : Fetcher.Factory<PagePreview> {
        override fun create(
            data: PagePreview,
            options: Options,
            imageLoader: ImageLoader
        ) = LoadingIndicatorFetcher(generator, data, options)
    }
}
