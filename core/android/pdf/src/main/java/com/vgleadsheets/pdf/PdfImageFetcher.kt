package com.vgleadsheets.pdf

import androidx.core.graphics.drawable.toDrawable
import coil.ImageLoader
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.Fetcher
import coil.request.Options
import coil.size.pxOrElse
import javax.inject.Inject

class PdfImageFetcher(
    private val pdfToBitmapRenderer: PdfToBitmapRenderer,
    private val data: PdfLoadConfig,
    private val options: Options
) : Fetcher {
    override suspend fun fetch(): DrawableResult {
        val width = options.size.width.pxOrElse { 320 }
        return DrawableResult(
            drawable = pdfToBitmapRenderer
                .renderPdfToBitmap(data, width)
                .toDrawable(options.context.resources),
            isSampled = true,
            dataSource = DataSource.MEMORY
        )
    }

    class Factory @Inject constructor(
        private val pdfToBitmapRenderer: PdfToBitmapRenderer,
    ) : Fetcher.Factory<PdfLoadConfig> {
        override fun create(
            data: PdfLoadConfig,
            options: Options,
            imageLoader: ImageLoader
        ) = PdfImageFetcher(pdfToBitmapRenderer, data, options)
    }
}
