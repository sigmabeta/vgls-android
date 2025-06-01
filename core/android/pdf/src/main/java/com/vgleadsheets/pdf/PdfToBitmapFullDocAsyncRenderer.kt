package com.vgleadsheets.pdf

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.pdf.PdfRenderer
import androidx.core.graphics.createBitmap
import com.vgleadsheets.bitmaps.BitmapSizeInfo
import com.vgleadsheets.bitmaps.BitmapUtils
import com.vgleadsheets.logging.Hatchet
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis

class PdfToBitmapFullDocAsyncRenderer(
    private val pdfRenderer: PdfRenderer,
    private val hatchet: Hatchet,
    private val maxWidth: Int,
    private val maxHeight: Int,
) : AsyncRenderer {
    override fun getActualDimensions(): Pair<Int, Int> {
        val bitmapSizeInfo = getBitmapSizeInfo(
            maxWidth,
            maxHeight,
        )
        return bitmapSizeInfo.docWidth to bitmapSizeInfo.pageHeight
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
            val renderProcessTime = measureTimeMillis {
                val largeBitmap = createABitmap(
                    pdfRenderer,
                    width,
                    height,
                    zoom,
                    dXPixels,
                    dYPixels
                )
                resultBitmap = largeBitmap.copy(Bitmap.Config.RGB_565, false)
                largeBitmap.recycle()

                hatchet.v("Result bitmap size: ${resultBitmap.byteCount / 1_024 / 1_024f} MiB.")
            }

            hatchet.v("Full PDF process took $renderProcessTime ms.")
            return resultBitmap
        } catch (ex: Exception) {
            hatchet.e("Failed to read PDF: ${ex.message}")
            throw ex
        }
    }

    private fun getBitmapSizeInfo(maxWidth: Int, maxHeight: Int): BitmapSizeInfo {
        val (pageWidth, pageHeight) = getPageDimensions()
        val pageCount = pdfRenderer.pageCount

        return BitmapUtils.computeBitmapSize(
            hatchet,
            pageCount,
            maxWidth,
            maxHeight,
            pageWidth,
            pageHeight,
            1f
        )
    }

    private fun getPageDimensions() = synchronized(pdfRenderer) {
        pdfRenderer
            .openPage(0)
            .use { it.width to it.height }
    }

    private fun createABitmap(
        pdfRenderer: PdfRenderer,
        width: Int,
        height: Int,
        zoom: Float,
        dXPixels: Int,
        dYPixels: Int,
    ): Bitmap {
        val pageCount = pdfRenderer.pageCount
        val (pageWidth, pageHeight) = getPageDimensions()
        val totalDocWidth = pageWidth * pageCount

        val bitmapSizeInfo = BitmapUtils.computeBitmapSize(
            hatchet,
            pageCount,
            width,
            height,
            totalDocWidth,
            pageHeight,
            zoom
        )

        val leftMostCoord = dXPixels
        val rightMostCoord = dXPixels + bitmapSizeInfo.pageWidth

        val scaledPageWidth = pageWidth * bitmapSizeInfo.zoomedScalingFactor
        val scaledDocWidth = (scaledPageWidth * pageCount).roundToInt()

        val firstPageToDisplay = max(0, (leftMostCoord / scaledPageWidth).toInt())
        val lastPageToDisplay = min(pageCount - 1, (rightMostCoord / scaledPageWidth).toInt())

        hatchet.v("Page width is $scaledPageWidth x Page count $pageCount Total doc width $scaledDocWidth")
        hatchet.v("Rendering columns $leftMostCoord through $rightMostCoord")
        hatchet.v("Rendering pages $firstPageToDisplay through $lastPageToDisplay")

        val newBitmap = createBlankBitmap(
            width = bitmapSizeInfo.pageWidth,
            height = bitmapSizeInfo.pageHeight,
        )

        for (pageNumber in firstPageToDisplay..lastPageToDisplay) {
            val pageDXPixels = dXPixels - (scaledPageWidth * pageNumber)
            hatchet.v("Rendering page $pageNumber, which starts at pixel column $pageDXPixels")

            synchronized(pdfRenderer) {
                pdfRenderer
                    .openPage(pageNumber)
                    .use { currentPage ->
                        val transformMatrix = defaultTransformMatrix(bitmapSizeInfo.zoomedScalingFactor)

                        transformMatrix.apply {
                            postTranslate(-pageDXPixels.toFloat(), -dYPixels.toFloat())
                        }

                        currentPage.render(
                            newBitmap,
                            null,
                            transformMatrix,
                            PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                        )
                    }
            }
        }

        BitmapUtils.renderDebugInfo(
            circle = false,
            text = false,
            border = true,
            bitmap = newBitmap,
            dXPixels = dXPixels,
            dYPixels = dYPixels,
            zoom = zoom,
            width = width,
            height = height
        )

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
