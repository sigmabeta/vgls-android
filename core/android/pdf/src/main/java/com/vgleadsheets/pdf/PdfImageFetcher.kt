package com.vgleadsheets.pdf

import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.request.Options
import com.vgleadsheets.downloader.SheetDownloader
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import javax.inject.Inject

class PdfImageFetcher(
    private val sheetDownloader: SheetDownloader,
    private val data: PdfConfigById,
    private val options: Options,
) : Fetcher {
    @OptIn(ExperimentalCoilApi::class)
    override suspend fun fetch(): SourceResult {
        val pdfFile = sheetDownloader.getSheet(data.songId, data.partApiId)
        val pdfPath = pdfFile.toOkioPath()

        val width = computeWidth(options)

        return SourceResult(
            source = ImageSource(
                file = pdfPath,
                fileSystem = FileSystem.SYSTEM,
                diskCacheKey = "${data.songId}-${data.partApiId}-${data.pageNumber}-$width",
                metadata = PdfMetadata(data.pageNumber)
            ),
            dataSource = DataSource.DISK,
            mimeType = MIMETYPE
        )
    }

    class Factory @Inject constructor(
        private val sheetDownloader: SheetDownloader,
    ) : Fetcher.Factory<PdfConfigById> {
        override fun create(
            data: PdfConfigById,
            options: Options,
            imageLoader: ImageLoader
        ) = PdfImageFetcher(
            sheetDownloader,
            data,
            options
        )
    }

    companion object {
        const val MIMETYPE = "application/pdf"
    }
}
