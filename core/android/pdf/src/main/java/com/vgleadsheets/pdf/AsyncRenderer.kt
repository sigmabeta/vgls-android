package com.vgleadsheets.pdf

import android.graphics.Bitmap

interface AsyncRenderer {
    fun getActualDimensions(): Pair<Int, Int>

    suspend fun renderToBitmap(
        width: Int,
        height: Int,
        zoom: Float,
        dXPixels: Int,
        dYPixels: Int,
    ): Bitmap
}
