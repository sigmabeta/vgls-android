package com.vgleadsheets.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.core.graphics.createBitmap
import com.vgleadsheets.logging.Hatchet
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

@Singleton
class PdfToBitmapRenderer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val hatchet: Hatchet,
) {
    private val backgroundPaint =
        Paint().apply {
            isAntiAlias = false
            color = Color.WHITE
        }

    fun renderPdfToBitmap(pdfLoadConfig: PdfLoadConfig, width: Int): Bitmap {
        val pdfPath = pdfLoadConfig.pdfPath
        val pageNumber = pdfLoadConfig.pageNumber

        val file = File(context.filesDir, pdfPath)
        hatchet.v("Generating $width px wide sheet bitmap for page $pageNumber of file ${file.name} ")

        val fileDescriptor = ParcelFileDescriptor.open(
            file,
            ParcelFileDescriptor.MODE_READ_ONLY
        )
        hatchet.v("Generating $width px wide sheet bitmap for page $pageNumber of file ${file.name} ")

        val pdfRenderer = PdfRenderer(fileDescriptor)
        val pageCount = pdfRenderer.pageCount

        if (pageNumber >= pageCount) {
            throw IllegalArgumentException("PDF only has $pageCount pages, can't render page $pageNumber.")
        }
        val bitmap = createBitmap(pdfRenderer, pageNumber, width)

        pdfRenderer.close()
        fileDescriptor.close()

        return bitmap
    }

    private fun createBitmap(
        pdfRenderer: PdfRenderer,
        pageNumber: Int,
        width: Int
    ): Bitmap {
        val newBitmap: Bitmap
        val openPage = pdfRenderer.openPage(pageNumber)

        val pdfRenderTime = measureTimeMillis {
            openPage
                .use { currentPage ->
                    val scalingFactor = width / currentPage.width.toFloat()
                    val scaledHeight = scalingFactor * currentPage.height

                    newBitmap = createBlankBitmap(
                        width = width,
                        height = scaledHeight.toInt()
                    )

                    currentPage.render(
                        newBitmap,
                        null,
                        null,
                        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                    )
                }
        }
        hatchet.v("PDF page rendering took $pdfRenderTime ms.")
        return newBitmap
    }

    private fun createBlankBitmap(
        width: Int,
        height: Int
    ): Bitmap {
        return createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
    }
}
