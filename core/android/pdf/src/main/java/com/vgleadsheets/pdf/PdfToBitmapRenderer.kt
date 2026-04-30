package com.vgleadsheets.pdf

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import net.sigmabeta.sage.android.bitmaps.BitmapUtils
import net.sigmabeta.sage.logging.Hatchet
import java.io.File
import kotlin.system.measureTimeMillis

@Suppress("MagicNumber")
class PdfToBitmapRenderer(
    private val hatchet: Hatchet,
) : BitmapRenderer {
    private var smallBitmap: Bitmap? = null
    private var fileDescriptor: ParcelFileDescriptor? = null
    private var pdfPath: String? = null
    private var pdfRenderer: PdfRenderer? = null

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
                    closeRenderer()

                    pdfPath = pdfFile.absolutePath
                    val descriptor = ParcelFileDescriptor.open(
                        pdfFile,
                        ParcelFileDescriptor.MODE_READ_ONLY
                    )

                    val newRenderer = PdfRenderer(descriptor)

                    pdfRenderer = newRenderer
                    fileDescriptor = descriptor

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

                closeRenderer()
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
                        pageCount = 1,
                        maxWidth,
                        maxHeight,
                        currentPage.width,
                        currentPage.height,
                        zoom
                    )

                    newBitmap = BitmapUtils.createBlankBitmap(
                        width = bitmapSizeInfo.pageWidth,
                        height = bitmapSizeInfo.pageHeight,
                    )

                    val transformMatrix = defaultTransformMatrix(bitmapSizeInfo.zoomedScalingFactor)

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
        val transformMatrix = Matrix()

        transformMatrix.postScale(
            scalingFactor,
            scalingFactor,
        )

        return transformMatrix
    }

    private fun closeRenderer() {
        pdfRenderer?.let {
            fileDescriptor?.close()
            it.close()
        }
    }
}
