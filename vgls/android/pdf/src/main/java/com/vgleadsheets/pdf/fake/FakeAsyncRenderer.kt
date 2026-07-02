package com.vgleadsheets.pdf.fake

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.vgleadsheets.bitmaps.BitmapUtils
import com.vgleadsheets.pdf.AsyncRenderer
import kotlin.system.measureTimeMillis

@Suppress("MagicNumber")
class FakeAsyncRenderer : AsyncRenderer {
    private val pageCount = 1
    private val pageWidth = 1236
    private val pageHeight = 1600

    override fun getActualDimensions() = (pageWidth * pageCount) to pageHeight

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun renderToBitmap(width: Int, height: Int, zoom: Float, dXPixels: Int, dYPixels: Int): Bitmap {
        var resultBitmap: Bitmap
        val renderProcessTime = measureTimeMillis {
            resultBitmap = BitmapUtils.createBitmapWithBackground(
                width = width,
                height = height,
            )
        }

        BitmapUtils.renderDebugInfo(
            circle = true,
            text = true,
            lines = true,
            bitmap = resultBitmap,
            renderTimeMs = renderProcessTime,
            dXPixels = dXPixels,
            dYPixels = dYPixels,
            width = width,
            height = height
        )

        return resultBitmap
    }
}
