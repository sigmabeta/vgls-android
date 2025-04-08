package com.vgleadsheets.pdf

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import com.vgleadsheets.downloader.SheetDownloader

class PdfStandaloneReader(
    private val sheetDownloader: SheetDownloader,
    private val bitmapRenderer: BitmapRenderer,
) : StandaloneReader {
    override suspend fun renderToDrawable(
        data: PdfConfigById,
        drawableWidth: Int,
        resources: Resources
    ): Drawable {
        val pdfFileResult = sheetDownloader.getSheet(data)
        val pdfFile = pdfFileResult.file

        val drawable = bitmapRenderer
            .renderToBitmap(pdfFile, data.pageNumber, drawableWidth)
            .toDrawable(resources)

        return drawable
    }
}
