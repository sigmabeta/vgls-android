package com.vgleadsheets.pdf

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.core.graphics.createBitmap
import com.vgleadsheets.logging.Hatchet
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

@Singleton
class PdfToBitmapRenderer @Inject constructor(
    private val hatchet: Hatchet,
) {
    private val backgroundPaint = Paint().apply {
        isAntiAlias = false
        color = Color.WHITE
    }

    fun renderPdfToBitmap(
        pdfFile: File,
        pdfConfigById: PdfConfigById,
        width: Int?,
    ): Bitmap {
        val pageNumber = pdfConfigById.pageNumber
        val fileDescriptor = ParcelFileDescriptor.open(
            pdfFile,
            ParcelFileDescriptor.MODE_READ_ONLY
        )

        hatchet.v("Generating sheet bitmap for page $pageNumber of file ${pdfFile.name} ")

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
        width: Int?
    ): Bitmap {
        val newBitmap: Bitmap
        val openPage = pdfRenderer.openPage(pageNumber)

        val pdfRenderTime = measureTimeMillis {
            openPage
                .use { currentPage ->
                    val (scaledWidth, scaledHeight) = if (width != null) {
                        val scalingFactor = width / currentPage.width.toFloat()
                        width to (scalingFactor * currentPage.height)
                    } else {
                        currentPage.width to currentPage.height
                    }

                    hatchet.v("Scaled width: $scaledWidth ")

                    newBitmap = createBlankBitmap(
                        width = scaledWidth,
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
        ).apply {
            val canvas = Canvas(this)

            canvas.drawRect(
                0.0f,
                0.0f,
                width.toFloat(),
                height.toFloat(),
                backgroundPaint
            )
        }
    }
}
