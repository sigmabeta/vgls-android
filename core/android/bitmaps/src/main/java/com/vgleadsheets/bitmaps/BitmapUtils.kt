package com.vgleadsheets.bitmaps

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.graphics.createBitmap
import com.vgleadsheets.logging.Hatchet
import kotlin.math.min
import kotlin.math.roundToInt

@Suppress("MagicNumber")
object BitmapUtils {
    fun createBitmapWithBackground(
        width: Int,
        height: Int,
    ): Bitmap {
        val backgroundPaint = Paint().apply {
            isAntiAlias = false
            color = Color.WHITE
        }

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
        text: Boolean = false,
        circle: Boolean = false,
        lines: Boolean = false,
        bitmap: Bitmap,
        dXPixels: Int,
        dYPixels: Int,
        renderTimeMs: Long,
        width: Int,
        height: Int
    ) {
        var canvas: Canvas? = null
        var tileColor: Int? = null

        if (text) {
            canvas = Canvas(bitmap)
            renderText(canvas, width, height, dXPixels, dYPixels, renderTimeMs)
        }

        if (circle) {
            if (canvas == null) {
                canvas = Canvas(bitmap)
            }

            tileColor = Pair(dXPixels, dYPixels).hashCode()
            renderCircle(tileColor, canvas, width, height)
        }

        if (lines) {
            if (canvas == null) {
                canvas = Canvas(bitmap)
            }

            if (tileColor == null) {
                tileColor = Pair(dXPixels, dYPixels).hashCode()
            }

            renderLines(tileColor, width, height, canvas)
        }
    }

    @Suppress("LongMethod")
    private fun renderText(
        canvas: Canvas,
        width: Int,
        height: Int,
        dXPixels: Int,
        dYPixels: Int,
        renderTimeMs: Long,
    ) {
        val textPaint = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            textSize = 96f
        }

        val bgPaint = Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            alpha = 192
        }

        val renderTimeBg = Rect(
            width * 9 / 16,
            height * 12 / 16,
            width * 15 / 16,
            height * 15 / 16,
        )

        val locationBg = Rect(
            width * 1 / 16,
            height * 1 / 16,
            width * 4 / 16,
            height * 3 / 16,
        )

        val widthBg = Rect(
            width * 6 / 16,
            height * 1 / 16,
            width * 10 / 16,
            height * 3 / 16,
        )

        val heightBg = Rect(
            width * 1 / 16,
            height * 7 / 16,
            width * 4 / 16,
            height * 9 / 16,
        )

        canvas.drawRect(renderTimeBg, bgPaint)
        canvas.drawText(
            "$renderTimeMs ms",
            width.toFloat() * 5 / 8,
            height.toFloat() * 7 / 8,
            textPaint,
        )

        textPaint.textSize = 40f

        canvas.drawRect(widthBg, bgPaint)
        canvas.drawText(
            "$width px",
            width.toFloat() * 7 / 16,
            height.toFloat() * 2 / 16,
            textPaint,
        )

        canvas.drawRect(heightBg, bgPaint)
        canvas.drawText(
            "$height px",
            width.toFloat() * 3 / 32,
            height.toFloat() / 2,
            textPaint,
        )

        textPaint.textSize = 20f

        canvas.drawRect(locationBg, bgPaint)
        canvas.drawText(
            "$dXPixels x $dYPixels\n",
            width.toFloat() * 3 / 32,
            height.toFloat() * 2 / 16,
            textPaint,
        )
    }

    private fun renderCircle(
        tileColor: Int,
        canvas: Canvas,
        width: Int,
        height: Int
    ) {
        val circlePaint = Paint().apply {
            isAntiAlias = true
            color = tileColor
            alpha = 255
        }

        canvas.drawCircle(
            width.toFloat() / 2,
            height.toFloat() / 2,
            min(width, height).toFloat() / 4,
            circlePaint
        )
    }

    private fun renderLines(
        tileColor: Int,
        width: Int,
        height: Int,
        canvas: Canvas
    ) {
        val borderPaint = Paint().apply {
            isAntiAlias = false
            strokeWidth = 8f
            color = tileColor
            alpha = 255
        }

        val centerPaint = Paint().apply {
            isAntiAlias = false
            strokeWidth = 16f
            color = tileColor
            alpha = 96
        }

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
            borderPaint,
        )

        val centerWidth = width / 2f
        val centerHeight = height / 2f
        val halfLeft = width / 4f
        val halfRight = width * 3 / 4f
        val halfTop = height / 4f
        val halfBottom = height * 3 / 4f

        canvas.drawLines(
            floatArrayOf(
                halfLeft, centerHeight, halfRight, centerHeight,
                centerWidth, halfTop, centerWidth, halfBottom,
                halfRight, centerHeight, halfLeft, centerHeight,
                centerWidth, halfBottom, centerWidth, halfTop,
            ),
            centerPaint,
        )
    }

    @Suppress("ReturnCount")
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
