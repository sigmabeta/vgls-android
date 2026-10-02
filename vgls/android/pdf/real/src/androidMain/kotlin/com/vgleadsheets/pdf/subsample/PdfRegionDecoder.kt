package com.vgleadsheets.pdf.subsample

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import com.vgleadsheets.pdf.AsyncRenderer
import com.vgleadsheets.pdf.PdfToBitmapAsyncRenderer
import com.vgleadsheets.pdf.PdfToBitmapFullDocAsyncRenderer
import com.vgleadsheets.pdf.ZOOM_MAX_PDF
import kotlinx.coroutines.withContext
import me.saket.telephoto.subsamplingimage.internal.ImageRegionDecoder
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.RenderOverlayProvider
import net.sigmabeta.sage.logging.BluntHatchet
import net.sigmabeta.sage.logging.Hatchet
import okio.Path
import kotlin.math.absoluteValue
import kotlin.math.min

class PdfRegionDecoder(
    private val pdfFile: Path,
    pageNumber: Int?,
    private val maxWidth: Int,
    private val maxHeight: Int,
    private val sageDispatchers: SageDispatchers,
    private val hatchet: Hatchet,
    private val renderOverlayProvider: RenderOverlayProvider,
) : ImageRegionDecoder {
    private val pdfEngine: PdfRenderer = createPdfRenderer(pdfFile)

    @Volatile
    private var closed = false

    @Volatile
    private var renderer: AsyncRenderer? =
//        FakeAsyncRenderer()
        if (pageNumber == null) {
            PdfToBitmapFullDocAsyncRenderer(
                pdfEngine,
                BluntHatchet(),
                maxWidth,
                maxHeight,
                renderOverlayProvider,
            )
        } else {
            PdfToBitmapAsyncRenderer(
                pdfEngine,
                BluntHatchet(),
                pageNumber,
                maxWidth,
                maxHeight,
                renderOverlayProvider,
            )
        }

    override val imageSize = requireNotNull(renderer)
        .getActualDimensions()
        .let {
            IntSize(
                it.first * ZOOM_MAX_PDF,
                it.second * ZOOM_MAX_PDF,
            )
        }

    override fun close() {
        hatchet.i("Closing PDF renderer for $pdfFile")
        renderer = null
        // Telephoto can close us while a decodeRegion() is mid-render on another thread. The renderers
        // lock on the engine around every page access, so closing under the same lock means a render
        // either finishes first or sees a fully-closed engine (IllegalStateException, handled below)
        // rather than a half-torn-down one (native NPE in PdfProcessor), and close() never lands
        // while a page is open ("Current page not closed").
        synchronized(pdfEngine) {
            closed = true
            pdfEngine.close()
        }
    }

    override suspend fun decodeRegion(region: IntRect, sampleSize: Int): ImageRegionDecoder.DecodeResult {
        val renderer: AsyncRenderer? = renderer

        if (renderer == null) {
            return blankResult()
        }

        val viewportSize = IntSize(maxWidth, maxHeight)
        val unscaledImageSize = imageSize
        val maxSampleSize = ImageSampleSize.calculateFor(
            viewportSize = viewportSize,
            scaledImageSize = unscaledImageSize
        )

        val zoom = maxSampleSize.size.toFloat() / sampleSize

        val regionBitmap = try {
            withContext(sageDispatchers.computation) {
                renderer.renderToBitmap(
                    width = region.width / sampleSize,
                    height = region.height / sampleSize,
                    zoom = zoom,
                    dXPixels = region.left / sampleSize,
                    dYPixels = region.top / sampleSize,
                )
            }
        } catch (ex: IllegalStateException) {
            if (!closed) throw ex
            hatchet.w("PDF renderer for $pdfFile closed mid-decode; returning blank tile.")
            return blankResult()
        }

        return ImageRegionDecoder.DecodeResult(
            painter = BitmapPainter(regionBitmap.asImageBitmap()),
            hasUltraHdrContent = false
        )
    }

    private fun blankResult() = ImageRegionDecoder.DecodeResult(
        painter = ColorPainter(Color.White),
        hasUltraHdrContent = false
    )

    private fun createPdfRenderer(pdfFile: Path): PdfRenderer {
        val fileDescriptor = ParcelFileDescriptor.open(
            pdfFile.toFile(),
            ParcelFileDescriptor.MODE_READ_ONLY
        )

        return PdfRenderer(fileDescriptor)
    }

    class Factory(
        private val pdfFile: Path,
        private val pageNumber: Int?,
        private val maxWidth: Int,
        private val maxHeight: Int,
        private val sageDispatchers: SageDispatchers,
        private val hatchet: Hatchet,
        private val renderOverlayProvider: RenderOverlayProvider,
    ) : ImageRegionDecoder.Factory {
        override suspend fun create(params: ImageRegionDecoder.FactoryParams): ImageRegionDecoder = PdfRegionDecoder(
                pdfFile,
                pageNumber,
                maxWidth,
                maxHeight,
                sageDispatchers,
                hatchet,
                renderOverlayProvider,
            )
    }
}

@JvmInline
private value class ImageSampleSize(val size: Int) {
    companion object {
        fun calculateFor(
            viewportSize: IntSize,
            scaledImageSize: IntSize
        ): ImageSampleSize {
            val viewportMinDimension = min(viewportSize.width.absoluteValue, viewportSize.height.absoluteValue)
            check(viewportMinDimension > 0f) { "Can't calculate a sample size for $viewportSize" }

            val zoom = minOf(
                viewportSize.width / scaledImageSize.width.toFloat(),
                viewportSize.height / scaledImageSize.height.toFloat()
            )
            return calculateFor(zoom)
        }

        fun calculateFor(zoom: Float): ImageSampleSize {
            if (zoom == 0f) {
                return ImageSampleSize(1)
            }

            var sampleSize = 1
            while (sampleSize * 2 <= (1 / zoom)) {
                // BitmapRegionDecoder requires values based on powers of 2.
                sampleSize *= 2
            }
            return ImageSampleSize(sampleSize)
        }
    }

    init {
        check(size == 1 || size.rem(2) == 0) {
            "Incorrect size = $size. BitmapRegionDecoder requires values based on powers of 2."
        }
    }
}
