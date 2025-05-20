package com.vgleadsheets.pdf.subsample

import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.pdf.HEIGHT_DEFAULT_PDF_4X_1080P
import com.vgleadsheets.pdf.PdfToBitmapAsyncRenderer
import com.vgleadsheets.pdf.WIDTH_DEFAULT_PDF_4X_1080P
import java.io.File
import kotlin.math.roundToInt
import me.saket.telephoto.subsamplingimage.internal.ImageRegionDecoder

class PdfRegionDecoder(
    pdfFile: File,
    pageNumber: Int,
    private val hatchet: Hatchet,
) : ImageRegionDecoder {
    private val renderer = PdfToBitmapAsyncRenderer(
        hatchet,
        pageNumber,
        pdfFile.absolutePath
    )
    override val imageSize = IntSize(
        WIDTH_DEFAULT_PDF_4X_1080P,
        HEIGHT_DEFAULT_PDF_4X_1080P,
    )

    override suspend fun decodeRegion(region: IntRect, sampleSize: Int): ImageRegionDecoder.DecodeResult {
        hatchet.v("Decoding samplesize $sampleSize region $region")
        val regionBitmap = renderer.renderToBitmap(
            width = (region.width.toFloat() / sampleSize).roundToInt(),
            height = (region.height.toFloat() / sampleSize).roundToInt(),
            zoom = 4f / sampleSize,
            dXPixels = region.left,
            dYPixels = region.top,
        )

        return ImageRegionDecoder.DecodeResult(
            painter = BitmapPainter(regionBitmap.asImageBitmap()),
            hasUltraHdrContent = false
        )
    }

    class Factory(
        private val pdfFile: File,
        private val pageNumber: Int,
        private val hatchet: Hatchet,
    ) : ImageRegionDecoder.Factory {
        override suspend fun create(params: ImageRegionDecoder.FactoryParams): ImageRegionDecoder {
            return PdfRegionDecoder(
                pdfFile,
                pageNumber,
                hatchet,
            )
        }
    }
}
