package com.vgleadsheets.pdf

import coil3.ImageLoader
import coil3.decode.DataSource
import coil3.decode.ImageSource
import coil3.fetch.Fetcher
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import com.vgleadsheets.downloader.SheetDownloader
import net.sigmabeta.sage.pdf.PdfConfigById
import com.vgleadsheets.downloader.SheetSourceType
import okio.FileSystem

class PdfImageFetcher(
    private val sheetDownloader: SheetDownloader,
    private val data: PdfConfigById,
) : Fetcher {
    override suspend fun fetch(): SourceFetchResult {
        val maxWidth = requireNotNull(data.maxWidth) { "PDFs must have a width specified." }
        val maxHeight = requireNotNull(data.maxHeight) { "PDFs must have a width specified." }
        val pageNumber = requireNotNull(data.pageNumber) { "PDFs must have a page number specified." }

        val pdfFileResult = sheetDownloader.getSheet(data)
        val pdfPath = pdfFileResult.path

        return SourceFetchResult(
            source = ImageSource(
                file = pdfPath,
                fileSystem = FileSystem.SYSTEM,
                metadata = PdfMetadata(
                    pageNumber,
                    maxWidth,
                    maxHeight,
                )
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
