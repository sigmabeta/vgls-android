package com.vgleadsheets.images

import androidx.compose.ui.graphics.ImageBitmap

object BitmapGenerator {
    fun generateBitmap(
        url: String,
        squareImage: Boolean = true,
        drawLines: Boolean = false
    ): ImageBitmap {
        throw RuntimeException("This shouldn't be used in release builds.")
    }
}
