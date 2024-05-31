package com.vgleadsheets.images

import android.animation.ArgbEvaluator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlin.random.Random

object BitmapGenerator {
    fun generateBitmap(
        sourceInfo: Any,
        squareImage: Boolean = true,
        drawLines: Boolean = false
    ): ImageBitmap {
        val rng = Random(sourceInfo.hashCode())
        val width = rng.nextInt(16) + 16
        val height = if (squareImage) {
            width
        } else {
            rng.nextInt(16) + 16
        }

        val sizeInBytes = width * height * BYTES_PER_PIXEL

        val byteArray = ByteArray(sizeInBytes)
        byteArray.forEachIndexed { index, _ ->
            byteArray[index] = index.toByte()
        }

        val bitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888,
        )

        val canvas = Canvas(bitmap)
        val paint = Paint()

        val colorTopLeft = rng.nextInt()
        val colorTopRight = rng.nextInt()
        val colorBottomLeft = rng.nextInt()
        val colorBottomRight = rng.nextInt()

        val evaluator = ArgbEvaluator()

        repeat(height) { row ->
            val heightFraction = row.toFloat() / (height - 1)

            val colorLeft = evaluator.evaluate(heightFraction, colorTopLeft, colorBottomLeft) as Int
            val colorRight = evaluator.evaluate(heightFraction, colorTopRight, colorBottomRight) as Int

            val shader = LinearGradient(
                0.0f,
                0.0f,
                width.toFloat(),
                1.0f,
                colorLeft,
                colorRight,
                Shader.TileMode.MIRROR,
            ) as Shader

            paint.shader = shader
            paint.isAntiAlias = false
            canvas.drawLine(0.0f, row.toFloat(), width.toFloat(), row.toFloat(), paint)
        }

        if (drawLines) {
            paint.color = Color.WHITE

            val leftVertLine = (width.toFloat() / 3) - 1
            val rightVertLine = (2 * width.toFloat() / 3)

            canvas.drawLine(leftVertLine, 0.0f, leftVertLine, height.toFloat(), paint)
            canvas.drawLine(rightVertLine, 0.0f, rightVertLine, height.toFloat(), paint)

            val topHorizLine = (height.toFloat() / 3) - 1
            val bottomHorizLine = 2 * height.toFloat() / 3

            canvas.drawLine(0.0f, topHorizLine, width.toFloat(), topHorizLine, paint)
            canvas.drawLine(0.0f, bottomHorizLine, width.toFloat(), bottomHorizLine, paint)
        }

        return bitmap.asImageBitmap()
    }

    private const val BYTES_PER_PIXEL = 4
}
