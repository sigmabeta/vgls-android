package com.vgleadsheets.pdf

import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.request.Options
import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.downloader.SheetSourceType
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
        val pdfFileResult = sheetDownloader.getSheet(data.songId, data.partApiId)
        val pdfFile = pdfFileResult.file

        val pdfPath = pdfFile.toOkioPath()

        val width = computeWidth(options)

        return SourceResult(
            source = ImageSource(
                file = pdfPath,
                fileSystem = FileSystem.SYSTEM,
                diskCacheKey = "pdf-${data.songId}-${data.partApiId}-${data.pageNumber}-$width",
                metadata = PdfMetadata(data.pageNumber)
            ),
            dataSource = pdfFileResult.sourceType.toCoilDataSource(),
            mimeType = MIMETYPE
        )
    }

    private fun SheetSourceType.toCoilDataSource() = when (this) {
        SheetSourceType.DISK -> DataSource.DISK
        SheetSourceType.NETWORK -> DataSource.NETWORK
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
