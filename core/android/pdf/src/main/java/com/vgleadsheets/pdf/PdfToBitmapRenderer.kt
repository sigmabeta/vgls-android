package com.vgleadsheets.pdf

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.core.graphics.createBitmap
import com.vgleadsheets.logging.Hatchet
import java.io.File
import kotlin.system.measureTimeMillis

class PdfToBitmapRenderer(
    private val hatchet: Hatchet,
) : BitmapRenderer {
    var smallBitmap: Bitmap? = null
    var pdfPath: String? = null
    var pdfRenderer: PdfRenderer? = null


    @Suppress("TooGenericExceptionCaught")
    override fun renderToBitmap(
        pdfFile: File?,
        pageNumber: Int,
        width: Int,
        height: Int,
        zoom: Float,
    ): Bitmap {
        requireNotNull(pdfFile) { "PDF files cannot be null in the actual implementation." }

        if (smallBitmap?.isRecycled == false) {
            hatchet.v("Recycling old bitmap.")
            smallBitmap?.recycle()
            smallBitmap = null
        }

        try {
            var resultBitmap: Bitmap
            val renderProcessTime = measureTimeMillis {
                hatchet.d("Generating sheet bitmap for page $pageNumber of file ${pdfFile.absolutePath} ")

                val localPdfRenderer = if (pdfPath == pdfFile.absolutePath) {
                    requireNotNull(pdfRenderer) { "PDF renderer should not be null, but somehow is?" }
                } else {
                    pdfRenderer?.let {
                        hatchet.v("Closing old renderer and opening a new one.")
                        it.close()
                    }

                    pdfPath = pdfFile.absolutePath
                    val fileDescriptor = ParcelFileDescriptor.open(
                        pdfFile,
                        ParcelFileDescriptor.MODE_READ_ONLY
                    )

                    val newRenderer = PdfRenderer(fileDescriptor)
                    pdfRenderer = newRenderer

                    fileDescriptor.close()
                    newRenderer
                }

                require(pageNumber <= localPdfRenderer.pageCount) {
                    "PDF only has ${localPdfRenderer.pageCount} pages, can't render page $pageNumber."
                }

                val largeBitmap = createBitmap(
                    localPdfRenderer,
                    pageNumber,
                    width,
                    height,
                    zoom,
                )

                resultBitmap = largeBitmap.copy(Bitmap.Config.ALPHA_8, false)
                smallBitmap = resultBitmap
                largeBitmap.recycle()

                hatchet.v("Result bitmap size: ${resultBitmap.byteCount / 1_024 / 1_024f} MiB.")
            }

            hatchet.v("Full PDF process took $renderProcessTime ms.")
            return resultBitmap
        } catch (ex: Exception) {
            hatchet.e("Failed to read PDF: ${ex.message}")
            if (pdfFile.exists()) {
                pdfFile.delete()
                hatchet.w("Deleted PDF file at ${pdfFile.path}")
            }
            throw ex
        }
    }

    private fun createBitmap(
        pdfRenderer: PdfRenderer,
        pageNumber: Int,
        maxWidth: Int,
        maxHeight: Int,
        zoom: Float,
    ): Bitmap {
        val newBitmap: Bitmap
        val openPage = pdfRenderer.openPage(pageNumber)

        val pdfRenderTime = measureTimeMillis {
            openPage
                .use { currentPage ->
                    val pageAspectRatio = currentPage.width.toFloat() / currentPage.height
                    val maxDimenAspectRatio = maxWidth.toFloat() / maxHeight

                    val pdfWidth = currentPage.width
                    val pdfHeight = currentPage.height

                    val pageToMaximumScalingFactor = computePageToMaxScalingFactor(maxWidth, maxHeight, pdfWidth, pdfHeight)
                    val zoomedScalingFactor = pageToMaximumScalingFactor * zoom

                    val zoomedWidth = (pdfWidth * zoomedScalingFactor).toInt()
                    val zoomedHeight = (pdfHeight * zoomedScalingFactor).toInt()

                    val scalingType = computeScalingType(maxWidth, maxHeight, zoomedWidth, zoomedHeight)

                    val bitmapWidth = computeBitmapWidth(scalingType, zoomedWidth, maxWidth)
                    val bitmapHeight = computeBitmapHeight(scalingType, zoomedHeight, maxHeight)

                    hatchet.v("PDF page aspect ratio: $pageAspectRatio")
                    hatchet.v("MaxDimen aspect ratio: $maxDimenAspectRatio")

                    hatchet.v("PDF to max scaling factor: $pageToMaximumScalingFactor")
                    hatchet.v("Zoomed     scaling factor: $zoomedScalingFactor")

                    hatchet.v("Maximum      dimensions: $maxWidth x $maxHeight")
                    hatchet.v("Specced page dimensions: $pdfWidth x $pdfHeight")
                    hatchet.v("Zoomed  page dimensions: $zoomedWidth x $zoomedHeight")
                    hatchet.v("Bitmap  page dimensions: $bitmapWidth x $bitmapHeight")
                    hatchet.v("Scaling type: $scalingType")

                    newBitmap = createBlankBitmap(
                        width = bitmapWidth,
                        height = bitmapHeight,
                    )

                    val transformMatrix = defaultTransformMatrix(zoomedScalingFactor)

                    val dXPercent = 0f
                    val dYPercent = 0f

                    val dXPixels = -zoomedWidth * dXPercent
                    val dYPixels = -zoomedHeight * dYPercent

                    transformMatrix.apply {
                        postTranslate(dXPixels, dYPixels)
                    }

                    currentPage.render(
                        newBitmap,
                        null,
                        transformMatrix,
                        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                    )
                }
        }
        hatchet.v("PDF page rendering took $pdfRenderTime ms.")
        return newBitmap
    }

    private fun computePageToMaxScalingFactor(
        maxWidth: Int,
        maxHeight: Int,
        pdfWidth: Int,
        pdfHeight: Int
    ): Float {
        return if (maxWidth < maxHeight) {
            maxWidth / pdfWidth.toFloat()
        } else {
            maxHeight / pdfHeight.toFloat()
        }
    }

    private fun computeScalingType(
        maxWidth: Int,
        maxHeight: Int,
        targetWidth: Int,
        targetHeight: Int,
    ): ScalingType {
        return if (targetWidth > maxWidth) {
            if (targetHeight > maxHeight) {
                ScalingType.MAX
            } else {
                ScalingType.FILL_WIDTH_ADJUST_HEIGHT
            }
        } else {
            if (targetHeight > maxHeight) {
                ScalingType.FILL_HEIGHT_ADJUST_WIDTH
            } else {
                ScalingType.NONE
            }
        }
    }

    private fun computeBitmapWidth(
        scalingType: ScalingType,
        targetWidth: Int,
        maxWidth: Int
    ): Int {
        return when (scalingType) {
            ScalingType.NONE, ScalingType.FILL_HEIGHT_ADJUST_WIDTH -> targetWidth
            ScalingType.FILL_WIDTH_ADJUST_HEIGHT, ScalingType.MAX -> return maxWidth
        }
    }

    private fun computeBitmapHeight(
        scalingType: ScalingType,
        targetHeight: Int,
        maxHeight: Int
    ): Int {
        return when (scalingType) {
            ScalingType.NONE, ScalingType.FILL_WIDTH_ADJUST_HEIGHT -> targetHeight
            ScalingType.FILL_HEIGHT_ADJUST_WIDTH, ScalingType.MAX -> return maxHeight
        }
    }

    private fun defaultTransformMatrix(
        scalingFactor: Float,
    ): Matrix {
        val transformMatrix = Matrix();

        transformMatrix.postScale(
            scalingFactor,
            scalingFactor,
        )

        return transformMatrix
    }

    private fun createBlankBitmap(
        width: Int,
        height: Int,
    ): Bitmap {
        hatchet.v("Creating blank bitmap with dimensions $width x $height")
        return createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
    }
}
