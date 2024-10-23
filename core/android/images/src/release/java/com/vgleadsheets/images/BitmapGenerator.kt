package com.vgleadsheets.images

import androidx.compose.ui.graphics.ImageBitmap

object BitmapGenerator {
    fun generateBitmap(
        sourceInfo: Any,
        squareImage: Boolean = true,
    ): ImageBitmap {
        throw RuntimeException("This shouldn't be used in release builds.")
    }
}
