package com.vgleadsheets.pdf.fake

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.createBitmap
import com.vgleadsheets.bitmaps.nextColor
import com.vgleadsheets.pdf.BitmapRenderer
import java.io.File
import kotlin.random.Random

class FakeBitmapRenderer : BitmapRenderer {
    private val backgroundPaint = Paint().apply {
        isAntiAlias = false
        color = Color.WHITE
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun renderToBitmap(
        pdfFile: File?,
        pageNumber: Int,
        width: Int,
        height: Int,
        zoom: Float,
    ): Bitmap {
        val newBitmap: Bitmap
        val scaledWidth = (width * 1f).toInt()
        val scaledHeight = (scaledWidth / ASPECT_RATIO).toInt()
        newBitmap = createBlankBitmap(
            width = scaledWidth,
            height = scaledHeight
        )
        val canvas = Canvas(newBitmap)
        val paint = Paint()

        val rng = Random((pdfFile?.hashCode() ?: 0) + pageNumber)

        val stripeCount = STRIPE_COUNT_MIN + rng.nextInt(STRIPE_COUNT_RANGE)
        val possibleColors = List(8 + rng.nextInt(24)) {
            rng.nextColor()
        }

        val switchOverPercentPerRow = SWITCH_SLOPE_MIN + (rng.nextInt(100) * SWITCH_SLOPE_RANGE)
        paint.isAntiAlias = false

        repeat(scaledHeight) { row ->
            val rowPercent = row.toFloat() / scaledHeight
            val firstColorIndex = possibleColors.size.times(rowPercent).toInt()

            println("Row percent $rowPercent firstColorIndex $firstColorIndex")

            val switchOverAtPercent = switchOverPercentPerRow * rowPercent * 100
            val switchOverAtPixel = scaledWidth * switchOverAtPercent

            val firstColor = possibleColors[firstColorIndex]
            paint.color = firstColor
            canvas.drawLine(0.0f, row.toFloat(), switchOverAtPixel, row.toFloat(), paint)

            val secondColorIndex = firstColorIndex.plus(stripeCount).mod(possibleColors.size)

            val secondColor = possibleColors[secondColorIndex]
            paint.color = secondColor
            canvas.drawLine(switchOverAtPixel, row.toFloat(), scaledWidth.toFloat(), row.toFloat(), paint)

            println("First color 0x${firstColor.toHexString()} Second color 0x${secondColor.toHexString()}")
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

const val SWITCH_SLOPE_MIN = 0.00001f
const val SWITCH_SLOPE_MAX = 0.0001f
const val SWITCH_SLOPE_RANGE = SWITCH_SLOPE_MAX - SWITCH_SLOPE_MIN
const val STRIPE_COUNT_MIN = 8
const val STRIPE_COUNT_MAX = 32
const val STRIPE_COUNT_RANGE = STRIPE_COUNT_MAX - STRIPE_COUNT_MIN
const val ASPECT_RATIO = 0.77272f
