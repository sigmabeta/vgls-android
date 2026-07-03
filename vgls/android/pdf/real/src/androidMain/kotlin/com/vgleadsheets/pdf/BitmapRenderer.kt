package com.vgleadsheets.pdf

import android.graphics.Bitmap
import okio.Path

interface BitmapRenderer {
    @Suppress("TooGenericExceptionCaught")
    suspend fun renderToBitmap(
        pdfFile: Path?,
        pageNumber: Int,
        width: Int = 160,
        height: Int = 160,
        zoom: Float,
    ): Bitmap
}
