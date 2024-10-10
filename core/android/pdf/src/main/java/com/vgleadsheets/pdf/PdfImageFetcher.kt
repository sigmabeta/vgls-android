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
import javax.inject.Inject
import okio.FileSystem
import okio.Path.Companion.toOkioPath

class PdfImageFetcher(
    private val sheetDownloader: SheetDownloader,
    private val data: PdfConfigById,
) : Fetcher {
    @OptIn(ExperimentalCoilApi::class)
    override suspend fun fetch(): SourceResult {
        val pdfFileResult = sheetDownloader.getSheet(data)
        val pdfFile = pdfFileResult.file

        val pdfPath = pdfFile.toOkioPath()

        return SourceResult(
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
        )
    }

    companion object {
        const val MIMETYPE = "application/pdf"
    }
}
