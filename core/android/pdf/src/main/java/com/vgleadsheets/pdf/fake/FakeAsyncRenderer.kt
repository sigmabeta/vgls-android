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

class FakeAsyncRenderer: AsyncRenderer {
    private val pageCount = 1
    private val pageWidth = 1236
    private val pageHeight = 1600

    override fun getActualDimensions() = (pageWidth * pageCount) to pageHeight

    private val textPaint =
        Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            textSize = 96f
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun renderToBitmap(width: Int, height: Int, zoom: Float, dXPixels: Int, dYPixels: Int): Bitmap {
        val newBitmap = createBlankBitmap(
            width = width,
            height = height,
        )

        val circlePaint = Paint().apply {
            isAntiAlias = false
            color = Triple(dXPixels, dYPixels, zoom).hashCode()
            alpha = 255
        }
        val canvas = Canvas(newBitmap)

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

        return newBitmap
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
