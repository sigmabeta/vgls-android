package com.vgleadsheets.pdf

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.core.graphics.createBitmap
import com.vgleadsheets.logging.Hatchet
import java.io.File
import kotlin.math.min
import kotlin.system.measureTimeMillis

class PdfToBitmapRenderer(
    private val hatchet: Hatchet,
) : BitmapRenderer {
    private val backgroundPaint = Paint().apply {
        isAntiAlias = false
        color = Color.WHITE
    }

    @Suppress("TooGenericExceptionCaught")
    override fun renderToBitmap(
        pdfFile: File?,
        pageNumber: Int,
        width: Int,
        height: Int,
        zoom: Float,
    ): Bitmap {
        requireNotNull(pdfFile)

        try {
            val fileDescriptor = ParcelFileDescriptor.open(
                pdfFile,
                ParcelFileDescriptor.MODE_READ_ONLY
            )

            hatchet.v("Generating sheet bitmap for page $pageNumber of file ${pdfFile.absolutePath} ")

            val pdfRenderer = PdfRenderer(fileDescriptor)
            val pageCount = pdfRenderer.pageCount

            require(pageNumber <= pageCount) {
                "PDF only has $pageCount pages, can't render page $pageNumber."
            }
            val bitmap = createBitmap(
                pdfRenderer,
                pageNumber,
                width,
                height,
                zoom,
            )

            pdfRenderer.close()
            fileDescriptor.close()

            // bitm

            return bitmap
        } catch (ex: Exception) {
            if (pdfFile.exists()) {
                pdfFile.delete()
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
                    val pageAspectRatio = currentPage.width.toFloat() / currentPage.height
                    val maxDimenAspectRatio = maxWidth.toFloat() / maxHeight

                    val pdfWidth = currentPage.width
                    val pdfHeight = currentPage.height

                    val pageToMaximumScalingFactor = maxWidth / pdfWidth.toFloat()
                    val zoomedScalingFactor = pageToMaximumScalingFactor * zoom

                    val zoomedWidth = (pdfWidth * zoomedScalingFactor).toInt()
                    val zoomedHeight = (pdfHeight * zoomedScalingFactor).toInt()

                    val bitmapWidth = min(maxWidth, zoomedWidth)
                    val bitmapHeight = min(maxHeight, zoomedHeight)

                    hatchet.v("PDF page aspect ratio: $pageAspectRatio")
                    hatchet.v("MaxDimen aspect ratio: $maxDimenAspectRatio")

                    hatchet.v("PDF to max scaling factor: $pageToMaximumScalingFactor")
                    hatchet.v("Zoomed     scaling factor: $zoomedScalingFactor")

                    hatchet.v("Maximum      dimensions: $maxWidth x $maxHeight")
                    hatchet.v("Specced page dimensions: $pdfWidth x $pdfHeight")
                    hatchet.v("Zoomed  page dimensions: $zoomedWidth x $zoomedHeight")
                    hatchet.v("Bitmap  page dimensions: $bitmapWidth x $bitmapHeight")

                    newBitmap = createBlankBitmap(
                        width = bitmapWidth,
                        height = bitmapHeight,
                    )

                    val transformMatrix = defaultTransformMatrix(zoomedScalingFactor)

                    val dXPercent = 0f
                    val dYPercent = 0f

                    val dXPixels = -zoomedWidth * dXPercent
                    val dYPixels = -zoomedHeight * dYPercent

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
}
