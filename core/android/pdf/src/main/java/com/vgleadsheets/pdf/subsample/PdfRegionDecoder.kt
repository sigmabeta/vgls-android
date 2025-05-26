package com.vgleadsheets.pdf.subsample

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.pdf.ZOOM_MAX_PDF
import com.vgleadsheets.pdf.fake.FakeAsyncRenderer
import java.io.File
import kotlinx.coroutines.withContext
import me.saket.telephoto.subsamplingimage.internal.ImageRegionDecoder

class PdfRegionDecoder(
    private val pdfFile: File,
    pageNumber: Int?,
    maxWidth: Int,
    maxHeight: Int,
    private val vglsDispatchers: VglsDispatchers,
    private val hatchet: Hatchet,
) : ImageRegionDecoder {
    private var pdfRenderer = createPdfRenderer(pdfFile.absolutePath)
    private val renderer = FakeAsyncRenderer()
    // if (pageNumber == null) {
    //     PdfToBitmapFullDocAsyncRenderer(
    //         pdfRenderer,
    //         hatchet,
    //         maxWidth,
    //         maxHeight,
    //     )
    // } else {
    //     PdfToBitmapAsyncRenderer(
    //         pdfRenderer,
    //         hatchet,
    //         pageNumber,
    //         maxWidth,
    //         maxHeight,
    //     )
    // }

    override val imageSize = renderer.getActualDimensions().let {
        IntSize(
            it.first * ZOOM_MAX_PDF,
            it.second * ZOOM_MAX_PDF,
        )
    }

    override fun close() {
        hatchet.i("Closing PDF renderer for ${pdfFile.absolutePath}")
        pdfRenderer.close()
    }

    override suspend fun decodeRegion(region: IntRect, sampleSize: Int): ImageRegionDecoder.DecodeResult {
        val zoom = ZOOM_MAX_PDF.toFloat() / sampleSize

        hatchet.i("Decoding samplesize $sampleSize region $region")
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

    private fun createPdfRenderer(pdfPath: String): PdfRenderer {
        val pdfFile = File(pdfPath)
        val fileDescriptor = ParcelFileDescriptor.open(
            pdfFile,
            ParcelFileDescriptor.MODE_READ_ONLY
        )

        return PdfRenderer(fileDescriptor)
    }

    class Factory(
        private val pdfFile: File,
        private val pageNumber: Int?,
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
