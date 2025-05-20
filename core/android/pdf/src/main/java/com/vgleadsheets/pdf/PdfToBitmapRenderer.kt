package com.vgleadsheets.pdf

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.vgleadsheets.bitmaps.BitmapUtils
import com.vgleadsheets.logging.Hatchet
import java.io.File
import kotlin.system.measureTimeMillis

class PdfToBitmapRenderer(
    private val hatchet: Hatchet,
) : BitmapRenderer {
    var smallBitmap: Bitmap? = null
    var pdfPath: String? = null
    var pdfRenderer: PdfRenderer? = null


    @Suppress("TooGenericExceptionCaught")
    override suspend fun renderToBitmap(
        pdfFile: File?,
        pageNumber: Int,
        width: Int,
        height: Int,
        zoom: Float,
    ): Bitmap {
        requireNotNull(pdfFile) { "PDF files cannot be null in the actual implementation." }

        if (smallBitmap?.isRecycled == false) {
            hatchet.v("Recycling old bitmap.")
            smallBitmap?.recycle()
            smallBitmap = null
        }

        try {
            var resultBitmap: Bitmap
            val renderProcessTime = measureTimeMillis {
                hatchet.d("Generating sheet bitmap for page $pageNumber of file ${pdfFile.absolutePath} ")

                val localPdfRenderer = if (pdfPath == pdfFile.absolutePath) {
                    requireNotNull(pdfRenderer) { "PDF renderer should not be null, but somehow is?" }
                } else {
                    pdfRenderer?.let {
                        hatchet.v("Closing old renderer and opening a new one.")
                        it.close()
                    }

                    pdfPath = pdfFile.absolutePath
                    val fileDescriptor = ParcelFileDescriptor.open(
                        pdfFile,
                        ParcelFileDescriptor.MODE_READ_ONLY
                    )

                    val newRenderer = PdfRenderer(fileDescriptor)
                    pdfRenderer = newRenderer

                    fileDescriptor.close()
                    newRenderer
                }

                require(pageNumber <= localPdfRenderer.pageCount) {
                    "PDF only has ${localPdfRenderer.pageCount} pages, can't render page $pageNumber."
                }

                val largeBitmap = createBitmap(
                    localPdfRenderer,
                    pageNumber,
                    width,
                    height,
                    zoom,
                )

                resultBitmap = largeBitmap.copy(Bitmap.Config.ALPHA_8, false)
                smallBitmap = resultBitmap
                largeBitmap.recycle()
                // delay(2L * min(width, height))

                hatchet.v("Result bitmap size: ${resultBitmap.byteCount / 1_024 / 1_024f} MiB.")
            }

            hatchet.v("Full PDF process took $renderProcessTime ms.")
            return resultBitmap
        } catch (ex: Exception) {
            hatchet.e("Failed to read PDF: ${ex.message}")
            if (pdfFile.exists()) {
                pdfFile.delete()
                hatchet.w("Deleted PDF file at ${pdfFile.path}")
            }
            throw ex
        }
    }

    private fun createBitmap(
        pdfRenderer: PdfRenderer,
        pageNumber: Int,
        maxWidth: Int,
        maxHeight: Int,
        zoom: Float,
    ): Bitmap {
        val newBitmap: Bitmap
        val openPage = pdfRenderer.openPage(pageNumber)

        val pdfRenderTime = measureTimeMillis {
            openPage
                .use { currentPage ->
                    val bitmapSizeInfo = BitmapUtils.computeBitmapSize(
                        hatchet,
                        maxWidth,
                        maxHeight,
                        currentPage.width,
                        currentPage.height,
                        zoom
                    )

                    newBitmap = BitmapUtils.createBlankBitmap(
                        width = bitmapSizeInfo.width,
                        height = bitmapSizeInfo.height,
                    )

                    val transformMatrix = defaultTransformMatrix(bitmapSizeInfo.zoomedScalingFactor)

                    val dXPercent = 0f
                    val dYPercent = 0f

                    val dXPixels = -bitmapSizeInfo.zoomedWidth * dXPercent
                    val dYPixels = -bitmapSizeInfo.zoomedHeight * dYPercent

                    transformMatrix.apply {
                        postTranslate(dXPixels, dYPixels)
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
        return newBitmap
    }

    private fun defaultTransformMatrix(
        scalingFactor: Float,
    ): Matrix {
        val transformMatrix = Matrix();

        transformMatrix.postScale(
            scalingFactor,
            scalingFactor,
        )

        return transformMatrix
    }
}
