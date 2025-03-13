package com.vgleadsheets.pdf

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import com.vgleadsheets.downloader.SheetDownloader

class PdfStandaloneReader(
    private val sheetDownloader: SheetDownloader,
    private val pdfToBitmapRenderer: PdfToBitmapRenderer,
) {
    fun renderToDrawable(resources: Resources): Drawable {

        val pdfFileResult = sheetDownloader.getSheet(data)
        val pdfFile = pdfFileResult.file

        val width = computeWidth(options)

        val drawable = pdfToBitmapRenderer
            .renderPdfToBitmap(pdfFile, metadata.pageNumber, width)
            .toDrawable(options.context.resources)

        return drawable
    }
}
