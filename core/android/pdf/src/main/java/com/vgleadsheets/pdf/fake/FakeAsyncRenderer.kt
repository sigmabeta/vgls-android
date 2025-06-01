package com.vgleadsheets.pdf.fake

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.createBitmap
import com.vgleadsheets.pdf.AsyncRenderer
import kotlin.math.min

class FakeAsyncRenderer : AsyncRenderer {
    private val pageCount = 5
    private val pageWidth = 1236
    private val pageHeight = 1600

    override fun getActualDimensions() = (pageWidth * pageCount) to pageHeight

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun renderToBitmap(width: Int, height: Int, zoom: Float, dXPixels: Int, dYPixels: Int): Bitmap {
        val newBitmap = createBlankBitmap(
            width = width,
            height = height,
        )

        renderDebugInfo(newBitmap, dXPixels, dYPixels, zoom, width, height)

        return newBitmap
    }

    private fun renderDebugInfo(
        newBitmap: Bitmap,
        dXPixels: Int,
        dYPixels: Int,
        zoom: Float,
        width: Int,
        height: Int
    ) {
        val textPaint = Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            textSize = 96f
        }

        val circlePaint = Paint().apply {
            isAntiAlias = true
        }

        val linePaint = Paint().apply {
            isAntiAlias = false
            strokeWidth = 8f
            alpha = 255
        }

        val canvas = Canvas(newBitmap)
        val tileColor = Triple(dXPixels, dYPixels, zoom).hashCode()

        circlePaint.color = tileColor
        circlePaint.alpha = 255

        linePaint.color = tileColor
        linePaint.alpha = 255

        canvas.drawCircle(
            width.toFloat() / 2,
            height.toFloat() / 2,
            min(width, height).toFloat() / 4,
            circlePaint
        )

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

    @RequiresApi(Build.VERSION_CODES.O)
    private val backgroundPaint = Paint().apply {
        isAntiAlias = false
        color = Color.valueOf(0.8f, 0.8f, 1f, 1f).toArgb()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
}
