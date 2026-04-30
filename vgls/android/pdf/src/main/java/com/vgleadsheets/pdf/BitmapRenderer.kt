package com.vgleadsheets.pdf

import android.graphics.Bitmap
import java.io.File

interface BitmapRenderer {
    @Suppress("TooGenericExceptionCaught")
    suspend fun renderToBitmap(
        pdfFile: File?,
        pageNumber: Int,
        width: Int = 160,
        height: Int = 160,
        zoom: Float,
    ): Bitmap
}
