package com.vgleadsheets.bitmaps

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.createBitmap
import com.vgleadsheets.logging.Hatchet
import kotlin.math.min
import kotlin.math.roundToInt

object BitmapUtils {
    fun computeBitmapSize(
        hatchet: Hatchet,
        pageCount: Int,
        maxWidth: Int,
        maxHeight: Int,
        docWidth: Int,
        docHeight: Int,
        zoom: Float,
    ): BitmapSizeInfo {
        val pageToMaximumScalingFactor = computePageToMaxScalingFactor(maxWidth, maxHeight, docWidth, docHeight)
        val zoomedScalingFactor = pageToMaximumScalingFactor * zoom

        val zoomedWidth = (docWidth * zoomedScalingFactor).roundToInt()
        val zoomedHeight = (docHeight * zoomedScalingFactor).roundToInt()

        val scalingType = computeScalingType(maxWidth, maxHeight, zoomedWidth, zoomedHeight)

        val bitmapWidth = computeBitmapWidth(scalingType, zoomedWidth, maxWidth)
        val bitmapHeight = computeBitmapHeight(scalingType, zoomedHeight, maxHeight)

        hatchet.v("PDF to max scaling factor: $pageToMaximumScalingFactor")
        hatchet.v("Zoomed     scaling factor: $zoomedScalingFactor")

        hatchet.v("Maximum      dimensions: $maxWidth x $maxHeight")
        hatchet.v("Specced page dimensions: $docWidth x $docHeight")

        hatchet.v("Zoomed  page dimensions: $zoomedWidth x $zoomedHeight")
        hatchet.v("Bitmap  page dimensions: $bitmapWidth x $bitmapHeight")
        hatchet.v("Scaling type: $scalingType")

        return BitmapSizeInfo(
            docWidth = bitmapWidth * pageCount,
            pageWidth = bitmapWidth,
            pageHeight = bitmapHeight,
            pageToMaximumScalingFactor = pageToMaximumScalingFactor,
            zoomedScalingFactor = zoomedScalingFactor,
            zoomedPageWidth = zoomedWidth,
            zoomedPageHeight = zoomedHeight
        )
    }

    fun createBlankBitmap(
        width: Int,
        height: Int,
    ): Bitmap {
        return createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
    }

    fun renderDebugInfo(
        circle: Boolean,
        text: Boolean,
        border: Boolean,
        bitmap: Bitmap,
        dXPixels: Int,
        dYPixels: Int,
        zoom: Float,
        width: Int,
        height: Int
    ) {
        var canvas: Canvas? = null
        var tileColor: Int? = null

        if (text) {
            canvas = Canvas(bitmap)

            val textPaint = Paint().apply {
                isAntiAlias = true
                color = Color.BLACK
                textSize = 96f
            }

            canvas.drawText(
                "Zoom $zoom\n",
                width.toFloat() / 16,
                height.toFloat() / 16,
                textPaint,
            )

            canvas.drawText(
                "Location $dXPixels x $dYPixels\n",
                width.toFloat() / 16,
                height.toFloat() / 8,
                textPaint,
            )

            canvas.drawText(
                "Dimens $width x $height",
                width.toFloat() / 16,
                height.toFloat() / 6,
                textPaint,
            )
        }

        if (circle) {
            if (canvas == null) {
                canvas = Canvas(bitmap)
            }

            tileColor = Triple(dXPixels, dYPixels, zoom).hashCode()

            val circlePaint = Paint().apply {
                isAntiAlias = true
            }

            circlePaint.color = tileColor
            circlePaint.alpha = 255

            canvas.drawCircle(
                width.toFloat() / 2,
                height.toFloat() / 2,
                min(width, height).toFloat() / 4,
                circlePaint
            )
        }

        if (border) {
            if (canvas == null) {
                canvas = Canvas(bitmap)
            }

            if (tileColor == null) {
                tileColor = Triple(dXPixels, dYPixels, zoom).hashCode()
            }

            val linePaint = Paint().apply {
                isAntiAlias = false
                strokeWidth = 8f
                alpha = 255
            }

            linePaint.color = tileColor
            linePaint.alpha = 255

            val top = 4f
            val left = 4f
            val right = width.toFloat() - 4f
            val bottom = height.toFloat() - 4f

            canvas.drawLines(
                floatArrayOf(
                    left, top, right, top,
                    right, top, right, bottom,
                    right, bottom, left, bottom,
                    left, bottom, left, top,
                ),
                linePaint,
            )
        }
    }

    private fun computeScalingType(
        maxWidth: Int,
        maxHeight: Int,
        targetWidth: Int,
        targetHeight: Int,
    ): ScalingType {
        if (maxWidth == maxHeight) {
            if (targetHeight > targetWidth) {
                return ScalingType.FILL_HEIGHT_ADJUST_WIDTH
            }

            return ScalingType.FILL_WIDTH_ADJUST_HEIGHT
        }

        if (targetWidth > maxWidth) {
            if (targetHeight > maxHeight) {
                return ScalingType.MAX
            }
            return ScalingType.FILL_WIDTH_ADJUST_HEIGHT
        }


        if (targetHeight > maxHeight) {
            return ScalingType.FILL_HEIGHT_ADJUST_WIDTH
        }

        if (maxHeight > maxWidth) {
            return ScalingType.FILL_WIDTH_ADJUST_HEIGHT
        }

        return ScalingType.FILL_HEIGHT_ADJUST_WIDTH
    }
}

private fun computeBitmapWidth(
    scalingType: ScalingType,
    targetWidth: Int,
    maxWidth: Int
): Int {
    return when (scalingType) {
        ScalingType.FILL_HEIGHT_ADJUST_WIDTH -> targetWidth
        ScalingType.FILL_WIDTH_ADJUST_HEIGHT, ScalingType.MAX -> return maxWidth
    }
}

private fun computeBitmapHeight(
    scalingType: ScalingType,
    targetHeight: Int,
    maxHeight: Int
): Int {
    return when (scalingType) {
        ScalingType.FILL_WIDTH_ADJUST_HEIGHT -> targetHeight
        ScalingType.FILL_HEIGHT_ADJUST_WIDTH, ScalingType.MAX -> return maxHeight
    }
}

fun computePageToMaxScalingFactor(
    maxWidth: Int,
    maxHeight: Int,
    pdfWidth: Int,
    pdfHeight: Int
): Float {
    return if (maxWidth < maxHeight) {
        maxWidth / pdfWidth.toFloat()
    } else {
        maxHeight / pdfHeight.toFloat()
    }
}
