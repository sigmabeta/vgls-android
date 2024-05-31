package com.vgleadsheets.pdf

import androidx.core.graphics.drawable.toDrawable
import coil.ImageLoader
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.Fetcher
import coil.request.Options
import coil.size.pxOrElse
import com.vgleadsheets.downloader.SheetDownloader
import javax.inject.Inject

class PdfImageFetcher(
    private val sheetDownloader: SheetDownloader,
    private val pdfToBitmapRenderer: PdfToBitmapRenderer,
    private val data: PdfConfigById,
    private val options: Options
) : Fetcher {
    override suspend fun fetch(): DrawableResult {
        val pdfFile = sheetDownloader.getSheet(data.songId, data.partApiId)
        val width = options.size.width.pxOrElse { 320 }

        return DrawableResult(
            drawable = pdfToBitmapRenderer
                .renderPdfToBitmap(pdfFile, data, width)
                .toDrawable(options.context.resources),
            isSampled = true,
            dataSource = DataSource.MEMORY
        )
    }

    class Factory @Inject constructor(
        private val sheetDownloader: SheetDownloader,
        private val pdfToBitmapRenderer: PdfToBitmapRenderer,
    ) : Fetcher.Factory<PdfConfigById> {
        override fun create(
            data: PdfConfigById,
            options: Options,
            imageLoader: ImageLoader
        ) = PdfImageFetcher(
            sheetDownloader,
            pdfToBitmapRenderer,
            data,
            options
        )
    }
}