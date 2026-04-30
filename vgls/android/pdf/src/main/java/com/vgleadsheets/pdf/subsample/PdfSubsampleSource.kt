package com.vgleadsheets.pdf.subsample

import androidx.compose.ui.graphics.ImageBitmap
import net.sigmabeta.sage.debug.RenderOverlayProvider
import net.sigmabeta.sage.coroutines.VglsDispatchers
import com.vgleadsheets.downloader.SheetDownloader
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.pdf.PdfConfigById
import me.saket.telephoto.subsamplingimage.SubSamplingImageSource

class PdfSubsampleSource(
    private val data: PdfConfigById,
    private val hatchet: Hatchet,
    private val sheetDownloader: SheetDownloader,
    private val vglsDispatchers: VglsDispatchers,
    private val renderOverlayProvider: RenderOverlayProvider,
) : SubSamplingImageSource {
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
            vglsDispatchers,
            hatchet,
            renderOverlayProvider,
        )
    }

    override val preview: ImageBitmap? = null

    class Factory(
        private val hatchet: Hatchet,
        private val sheetDownloader: SheetDownloader,
        private val vglsDispatchers: VglsDispatchers,
        private val renderOverlayProvider: RenderOverlayProvider,
    ) : PdfSubsampleSourceFactory {
        override fun create(
            data: PdfConfigById,
        ) = PdfSubsampleSource(
            data,
            hatchet,
            sheetDownloader,
            vglsDispatchers,
            renderOverlayProvider,
        )
    }
}
