package com.vgleadsheets.images

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.lerp
import kotlin.random.Random

/**
 * Deterministic placeholder-image generator used only for `@Preview`/inspection (see
 * `CrossfadeImage`'s `forceGenBitmap = LocalInspectionMode.current`). Draws a random per-row
 * gradient into a Compose [ImageBitmap] with multiplatform graphics, so it lives in commonMain —
 * replacing the old android.graphics debug/release variant pair.
 */
object BitmapGenerator {
    fun generateBitmap(
        sourceInfo: Any,
        squareImage: Boolean = true,
    ): ImageBitmap {
        val rng = Random(sourceInfo.hashCode())
        val width = rng.nextInt(RNG_LIMIT) + RNG_LIMIT
        val height = if (squareImage) width else rng.nextInt(RNG_LIMIT) + RNG_LIMIT

        val bitmap = ImageBitmap(width, height)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply { isAntiAlias = false }

        val colorTopLeft = Color(rng.nextInt())
        val colorTopRight = Color(rng.nextInt())
        val colorBottomLeft = Color(rng.nextInt())
        val colorBottomRight = Color(rng.nextInt())

        repeat(height) { row ->
            val heightFraction = if (height > 1) row.toFloat() / (height - 1) else 0f
            val colorLeft = lerp(colorTopLeft, colorBottomLeft, heightFraction)
            val colorRight = lerp(colorTopRight, colorBottomRight, heightFraction)

            paint.shader = LinearGradientShader(
                from = Offset(0f, 0f),
                to = Offset(width.toFloat(), 1f),
                colors = listOf(colorLeft, colorRight),
                tileMode = TileMode.Mirror,
            )
            canvas.drawLine(
                Offset(0f, row.toFloat()),
                Offset(width.toFloat(), row.toFloat()),
                paint,
            )
        }

        return bitmap
    }

    private const val RNG_LIMIT = 16
}
