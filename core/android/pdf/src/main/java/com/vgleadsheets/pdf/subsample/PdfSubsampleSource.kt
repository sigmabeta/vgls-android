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

        return PdfRegionDecoder.Factory(
            pdfFile,
            data.pageNumber,
            hatchet,
        )
    }

    override val preview: ImageBitmap? = null

    override fun close() {
        super.close()
    }

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
