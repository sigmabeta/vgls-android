package com.vgleadsheets.pdf

import coil3.ImageLoader
import coil3.decode.DataSource
import coil3.decode.ImageSource
import coil3.fetch.Fetcher
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.downloader.SheetSourceType
import okio.FileSystem
import okio.Path.Companion.toOkioPath

class PdfImageFetcher(
    private val sheetDownloader: SheetDownloader,
    private val data: PdfConfigById,
) : Fetcher {
    override suspend fun fetch(): SourceFetchResult {
        println("Fetch request for $data")
        val pdfFileResult = sheetDownloader.getSheet(data)
        val pdfFile = pdfFileResult.file

        val pdfPath = pdfFile.toOkioPath()

        return SourceFetchResult(
            source = ImageSource(
                file = pdfPath,
                fileSystem = FileSystem.SYSTEM,
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

    class Factory(
        private val sheetDownloader: SheetDownloader,
    ) : Fetcher.Factory<PdfConfigById> {
        override fun create(
            data: PdfConfigById,
            options: Options,
            imageLoader: ImageLoader
        ) = PdfImageFetcher(
            sheetDownloader,
            data,
        )
    }

    companion object {
        const val MIMETYPE = "application/pdf"
    }
}
