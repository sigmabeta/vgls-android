package com.vgleadsheets.pdf.fake

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.createBitmap
import com.vgleadsheets.pdf.BitmapRenderer
import java.io.File
import kotlin.random.Random

class FakeBitmapRenderer : BitmapRenderer {
    private val backgroundPaint = Paint().apply {
        isAntiAlias = false
        color = Color.WHITE
    }

    override fun renderToBitmap(pdfFile: File?, pageNumber: Int, width: Int?): Bitmap {
        val newBitmap: Bitmap
        val scaledWidth = ((width ?: WIDTH_ARBITRARY) * 1f).toInt()
        val scaledHeight = (scaledWidth / ASPECT_RATIO).toInt()
        newBitmap = createBlankBitmap(
            width = scaledWidth,
            height = scaledHeight
        )
        val canvas = Canvas(newBitmap)
        val paint = Paint()

        val possibleColors = listOf(
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.BLACK,
            Color.CYAN,
            Color.GRAY,
            Color.MAGENTA,
            Color.YELLOW
        )

        repeat(scaledHeight) { row ->
            val firstColorIndex = row
                .div(STRIPE_HEIGHT_PX)
                .mod(possibleColors.size)

            paint.color = possibleColors[firstColorIndex]
            paint.isAntiAlias = false
            canvas.drawLine(0.0f, row.toFloat(), row.toFloat(), row.toFloat(), paint)

            val secondColorIndex = Random(row / STRIPE_HEIGHT_PX).nextInt()
                .mod(possibleColors.size)

            paint.color = possibleColors[secondColorIndex]
            paint.isAntiAlias = false
            canvas.drawLine(row.toFloat(), row.toFloat(), scaledWidth.toFloat(), row.toFloat(), paint)
        }

        val bitmap = newBitmap
        return bitmap
    }

    private fun createBlankBitmap(
        width: Int,
        height: Int
    ): Bitmap {
        return createBitmap(
            width,
            height,
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

const val STRIPE_HEIGHT_PX = 10
const val ASPECT_RATIO = 0.77272f
private const val WIDTH_ARBITRARY = 69
