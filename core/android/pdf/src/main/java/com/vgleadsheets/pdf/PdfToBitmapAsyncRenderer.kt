package com.vgleadsheets.pdf

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import com.vgleadsheets.bitmaps.BitmapSizeInfo
import com.vgleadsheets.bitmaps.BitmapUtils
import com.vgleadsheets.logging.Hatchet
import kotlin.system.measureTimeMillis

class PdfToBitmapAsyncRenderer(
    private val pdfRenderer: PdfRenderer,
    private val hatchet: Hatchet,
    private val pageNumber: Int,
    private val maxWidth: Int,
    private val maxHeight: Int,
) : AsyncRenderer {
    override fun getActualDimensions(): Pair<Int, Int> {
        val bitmapSizeInfo = getBitmapSizeInfo(
            maxWidth,
            maxHeight,
        ) return bitmapSizeInfo.pageWidth to bitmapSizeInfo.pageHeight
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun renderToBitmap(
        width: Int,
        height: Int,
        zoom: Float,
        dXPixels: Int,
        dYPixels: Int,
    ): Bitmap {
        hatchet.v("Rendering $width x $height zoom $zoom offset $dXPixels x $dYPixels")
        try {
            var resultBitmap: Bitmap
            val largeBitmap: Bitmap
            val renderProcessTime = measureTimeMillis {
                require(pageNumber <= pdfRenderer.pageCount) {
                    "PDF only has ${pdfRenderer.pageCount} pages, can't render page $pageNumber."
                }

                largeBitmap = createABitmap(
                    pdfRenderer,
                    pageNumber,
                    width,
                    height,
                    zoom,
                    dXPixels,
                    dYPixels
                )
            }

            BitmapUtils.renderDebugInfo(
                bitmap = largeBitmap,
                dXPixels = dXPixels,
                dYPixels = dYPixels,
                renderTimeMs = renderProcessTime,
                width = width,
                height = height
            )

            resultBitmap = largeBitmap.copy(Bitmap.Config.RGB_565, false)
            largeBitmap.recycle()

            hatchet.v("Result bitmap size: ${resultBitmap.byteCount / 1_024 / 1_024f} MiB.")
            hatchet.v("Full PDF process took $renderProcessTime ms.")
            return resultBitmap
        } catch (ex: Exception) {
            hatchet.e("Failed to read PDF: ${ex.message}")
            throw ex
        }
    }

    private fun getBitmapSizeInfo(maxWidth: Int, maxHeight: Int): BitmapSizeInfo {
        val (docWidth, docHeight) = pdfRenderer
            .openPage(pageNumber)
            .use { it.width to it.height }

        return BitmapUtils.computeBitmapSize(
            hatchet,
            pageCount = 1,
            maxWidth,
            maxHeight,
            docWidth,
            docHeight,
            1f
        )
    }

    private fun createABitmap(
        pdfRenderer: PdfRenderer,
        pageNumber: Int,
        width: Int,
        height: Int,
        zoom: Float,
        dXPixels: Int,
        dYPixels: Int,
    ): Bitmap {
        val newBitmap: Bitmap

        synchronized(pdfRenderer) {
            val pdfRenderTime = measureTimeMillis {
                pdfRenderer.openPage(pageNumber)
                    .use { currentPage ->
                        val bitmapSizeInfo = BitmapUtils.computeBitmapSize(
                            hatchet,
                            pageCount = 1,
                            width,
                            height,
                            currentPage.width,
                            currentPage.height,
                            zoom
                        )

                        newBitmap = BitmapUtils.createBitmapWithBackground(
                            width = bitmapSizeInfo.pageWidth,
                            height = bitmapSizeInfo.pageHeight,
                        )

                        val transformMatrix = defaultTransformMatrix(bitmapSizeInfo.zoomedScalingFactor)

                        transformMatrix.apply {
                            postTranslate(-dXPixels.toFloat(), -dYPixels.toFloat())
                        }

                        currentPage.render(
                            newBitmap,
                            null,
                            transformMatrix,
                            PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                        )
                    }
            }
            hatchet.v("PDF page rendering took $pdfRenderTime ms.")
        }
        return newBitmap
    }
}
