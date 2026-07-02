package com.vgleadsheets.pdf.subsample

import androidx.compose.ui.graphics.ImageBitmap
import com.vgleadsheets.downloader.SheetDownloader
import java.io.File
import me.saket.telephoto.subsamplingimage.SubSamplingImageSource
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.RenderOverlayProvider
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.pdf.PdfConfigById

class PdfSubsampleSource(
    private val data: PdfConfigById,
    private val hatchet: Hatchet,
    private val sheetDownloader: SheetDownloader,
    private val sageDispatchers: SageDispatchers,
    private val renderOverlayProvider: RenderOverlayProvider,
) : SubSamplingImageSource {
    override suspend fun decoder(): PdfRegionDecoder.Factory {
        val pdfFileResult = sheetDownloader.getSheet(data)
        // The interface returns an okio.Path (commonMain); the platform PDF renderer needs a File.
        val pdfFile = File(pdfFileResult.path.toString())

        val maxWidth = requireNotNull(data.maxWidth) { "Max Width is required." }
        val maxHeight = requireNotNull(data.maxHeight) { "Max Height is required." }

        return PdfRegionDecoder.Factory(
            pdfFile,
            data.pageNumber,
            maxWidth,
            maxHeight,
            sageDispatchers,
            hatchet,
            renderOverlayProvider,
        )
    }

    override val preview: ImageBitmap? = null

    class Factory(
        private val hatchet: Hatchet,
        private val sheetDownloader: SheetDownloader,
        private val sageDispatchers: SageDispatchers,
        private val renderOverlayProvider: RenderOverlayProvider,
    ) : PdfSubsampleSourceFactory {
        override fun create(
            data: PdfConfigById,
        ) = PdfSubsampleSource(
            data,
            hatchet,
            sheetDownloader,
            sageDispatchers,
            renderOverlayProvider,
        )
    }
}
