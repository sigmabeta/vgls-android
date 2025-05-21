package com.vgleadsheets.pdf.subsample

import androidx.compose.ui.graphics.ImageBitmap
import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.pdf.PdfConfigById
import me.saket.telephoto.subsamplingimage.SubSamplingImageSource

class PdfSubsampleSource(
    private val data: PdfConfigById,
    private val hatchet: Hatchet,
    private val sheetDownloader: SheetDownloader,
): SubSamplingImageSource {
    override suspend fun decoder(): PdfRegionDecoder.Factory {
        val pdfFileResult = sheetDownloader.getSheet(data)
        val pdfFile = pdfFileResult.file

        val maxWidth = requireNotNull(data.maxWidth) { "Max Width is required." }
        val maxHeight = requireNotNull(data.maxHeight) { "Max Height is required." }

        return PdfRegionDecoder.Factory(
            pdfFile,
            data.pageNumber,
            maxWidth,
            maxHeight,
            hatchet,
        )
    }

    override val preview: ImageBitmap? = null

    class Factory(
        private val hatchet: Hatchet,
        private val sheetDownloader: SheetDownloader,
    ): PdfSubsampleSourceFactory {
        override fun create(
            data: PdfConfigById,
        ) = PdfSubsampleSource(
            data,
            hatchet,
            sheetDownloader,
        )
    }
}
