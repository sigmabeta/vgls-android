package com.vgleadsheets.pdf.fake

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.vgleadsheets.bitmaps.BitmapUtils
import com.vgleadsheets.pdf.AsyncRenderer

class FakeAsyncRenderer : AsyncRenderer {
    private val pageCount = 5
    private val pageWidth = 1236
    private val pageHeight = 1600

    override fun getActualDimensions() = (pageWidth * pageCount) to pageHeight

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun renderToBitmap(width: Int, height: Int, zoom: Float, dXPixels: Int, dYPixels: Int): Bitmap {
        val newBitmap = BitmapUtils.createBitmapWithBackground(
            width = width,
            height = height,
        )

        BitmapUtils.renderDebugInfo(
            circle = false,
            text = false,
            border = true,
            bitmap = newBitmap,
            dXPixels = dXPixels,
            dYPixels = dYPixels,
            zoom = zoom,
            width = width,
            height = height
        )

        return newBitmap
    }
}
