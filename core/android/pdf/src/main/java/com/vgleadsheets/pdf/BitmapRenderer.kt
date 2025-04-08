package com.vgleadsheets.pdf

import android.graphics.Bitmap
import java.io.File

interface BitmapRenderer {
    @Suppress("TooGenericExceptionCaught")
    fun renderToBitmap(
        pdfFile: File?,
        pageNumber: Int,
        width: Int?,
    ): Bitmap
}
