package com.vgleadsheets.pdf.subsample

import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import com.vgleadsheets.bitmaps.BitmapUtils
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.pdf.PdfToBitmapAsyncRenderer
import com.vgleadsheets.pdf.ZOOM_MAX_PDF
import java.io.File
import kotlinx.coroutines.withContext
import me.saket.telephoto.subsamplingimage.internal.ImageRegionDecoder

class PdfRegionDecoder(
    pdfFile: File,
    pageNumber: Int,
    maxWidth: Int,
    maxHeight: Int,
    private val vglsDispatchers: VglsDispatchers,
    private val hatchet: Hatchet,
) : ImageRegionDecoder {
    private val renderer = PdfToBitmapAsyncRenderer(
        hatchet,
        pageNumber,
        pdfFile.absolutePath
    )

    private val actualWidth: Int
    private val actualHeight: Int

    init {
        val bitmapSize = BitmapUtils.computeBitmapSize(
            hatchet,
            maxWidth,
            maxHeight,
            612,
            792,
            1f
        )
        actualWidth = bitmapSize.width
        actualHeight = bitmapSize.height
    }

    override val imageSize = IntSize(
        actualWidth * ZOOM_MAX_PDF,
        actualHeight * ZOOM_MAX_PDF,
    )

    override fun close() {
        renderer.close()
    }

    override suspend fun decodeRegion(region: IntRect, sampleSize: Int): ImageRegionDecoder.DecodeResult {
        val zoom = ZOOM_MAX_PDF.toFloat() / sampleSize

        val regionBitmap = withContext(vglsDispatchers.computation) {
            renderer.renderToBitmap(
                width = region.width / sampleSize,
                height = region.height / sampleSize,
                zoom = zoom,
                dXPixels = region.left / sampleSize,
                dYPixels = region.top / sampleSize,
            )
        }

        return ImageRegionDecoder.DecodeResult(
            painter = BitmapPainter(regionBitmap.asImageBitmap()),
            hasUltraHdrContent = false
        )
    }

    class Factory(
        private val pdfFile: File,
        private val pageNumber: Int,
        private val maxWidth: Int,
        private val maxHeight: Int,
        private val vglsDispatchers: VglsDispatchers,
        private val hatchet: Hatchet,
    ) : ImageRegionDecoder.Factory {
        override suspend fun create(params: ImageRegionDecoder.FactoryParams): ImageRegionDecoder {
            return PdfRegionDecoder(
                pdfFile,
                pageNumber,
                maxWidth,
                maxHeight,
                vglsDispatchers,
                hatchet,
            )
        }
    }
}
