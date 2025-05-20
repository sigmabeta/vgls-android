package com.vgleadsheets.pdf

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.core.graphics.createBitmap
import com.vgleadsheets.bitmaps.computePageToMaxScalingFactor
import com.vgleadsheets.logging.Hatchet
import java.io.File
import kotlin.system.measureTimeMillis

class PdfToBitmapAsyncRenderer(
    private val hatchet: Hatchet,
    private val pageNumber: Int,
    private val pdfPath: String,
) {
    private var pdfRenderer: PdfRenderer? = null

    @Suppress("TooGenericExceptionCaught")
    suspend fun renderToBitmap(
        width: Int,
        height: Int,
        zoom: Float,
        dXPixels: Int,
        dYPixels: Int,
    ): Bitmap {
        hatchet.v("Rendering $width x $height zoom $zoom offset $dXPixels x $dYPixels")
        val pdfFile = File(pdfPath)
        try {
            var resultBitmap: Bitmap
            val renderProcessTime = measureTimeMillis {
                val localPdfRenderer = if (pdfRenderer != null) {
                    requireNotNull(pdfRenderer)
                } else {
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

                resultBitmap = createABitmap(
                    localPdfRenderer,
                    pageNumber,
                    width,
                    height,
                    zoom,
                    dXPixels,
                    dYPixels
                )

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

    private suspend fun createABitmap(
        pdfRenderer: PdfRenderer,
        pageNumber: Int,
        width: Int,
        height: Int,
        zoom: Float,
        dXPixels: Int,
        dYPixels: Int,
    ): Bitmap {
        val newBitmap: Bitmap
        val openPage = pdfRenderer.openPage(pageNumber)

        val pdfRenderTime = measureTimeMillis {
            openPage
                .use { currentPage ->
                     newBitmap = createBlankBitmap(
                        width = width,
                        height = height,
                    )

                    val pageToMaximumScalingFactor = computePageToMaxScalingFactor(
                        width,
                        height,
                        currentPage.width,
                        currentPage.height,
                    )

                    val zoomedScalingFactor = pageToMaximumScalingFactor * zoom
                    val transformMatrix = defaultTransformMatrix(zoomedScalingFactor)

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
        return newBitmap
    }

    private val backgroundPaint = Paint().apply {
        isAntiAlias = false
        color = Color.WHITE
    }

    private fun createBlankBitmap(
        width: Int,
        height: Int,
    ): Bitmap {
        hatchet.v("Creating blank bitmap with dimensions $width x $height")
        return createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        ).apply {
            val canvas = Canvas(this)

            canvas.drawRect(
                0.0f,
                0.0f,
                width.toFloat(),
                height.toFloat(),
                backgroundPaint
            )
        }
    }


    // private suspend fun createBitmap(
    //     pdfRenderer: PdfRenderer,
    //     pageNumber: Int,
    //     maxWidth: Int,
    //     maxHeight: Int,
    //     zoom: Float,
    //     dXPercent: Float,
    //     dYPercent: Float,
    // ): Bitmap {
    //     val newBitmap: Bitmap
    //     val openPage = pdfRenderer.openPage(pageNumber)
    //
    //     val pdfRenderTime = measureTimeMillis {
    //         openPage
    //             .use { currentPage ->
    //                 val bitmapSizeInfo = BitmapUtils.computeBitmapSize(
    //                     hatchet,
    //                     maxWidth,
    //                     maxHeight,
    //                     currentPage.width,
    //                     currentPage.height,
    //                     zoom
    //                 )
    //
    //                 newBitmap = BitmapUtils.createBlankBitmap(
    //                     width = bitmapSizeInfo.width,
    //                     height = bitmapSizeInfo.height,
    //                 )
    //
    //                 val transformMatrix = defaultTransformMatrix(bitmapSizeInfo.zoomedScalingFactor)
    //
    //                 val dXPixels = -bitmapSizeInfo.zoomedWidth * dXPercent
    //                 val dYPixels = -bitmapSizeInfo.zoomedHeight * dYPercent
    //
    //                 transformMatrix.apply {
    //                     postTranslate(dXPixels, dYPixels)
    //                 }
    //
    //                 currentPage.render(
    //                     newBitmap,
    //                     null,
    //                     transformMatrix,
    //                     PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
    //                 )
    //             }
    //     }
    //     hatchet.v("PDF page rendering took $pdfRenderTime ms.")
    //     return newBitmap
    // }

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
