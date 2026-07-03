package com.vgleadsheets.pdf

import coil3.ImageLoader
import coil3.asImage
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import net.sigmabeta.sage.logging.Hatchet
import org.apache.pdfbox.Loader
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import java.awt.image.BufferedImage

/**
 * Desktop equivalent of the android [PdfImageDecoder]: renders a single PDF page to pixels for Coil.
 * Android uses [android.graphics.pdf.PdfRenderer]; the JVM target has neither that nor telephoto, so
 * PDFBox rasterises the page to a [BufferedImage] which is copied into a skia [Bitmap] and wrapped as
 * a [coil3.Image]. The desktop viewer is non-zooming, so this one-shot full-page render is enough.
 */
class PdfBoxImageDecoder(
    private val hatchet: Hatchet,
    private val result: SourceFetchResult,
    private val options: Options,
) : Decoder {
    @Suppress("ReturnCount")
    override suspend fun decode(): DecodeResult? {
        if (result.mimeType != PdfImageFetcher.MIMETYPE) {
            return null
        }

        val metadata = result.source.metadata
        if (metadata !is PdfMetadata) {
            return null
        }

        val pdfFile = result.source.file()

        val bufferedImage = Loader.loadPDF(pdfFile.toFile()).use { document ->
            require(metadata.pageNumber < document.numberOfPages) {
                "PDF only has ${document.numberOfPages} pages, can't render page ${metadata.pageNumber}."
            }

            val page = document.getPage(metadata.pageNumber)
            val scale = computeScale(
                pageWidthPts = page.cropBox.width,
                pageHeightPts = page.cropBox.height,
                maxWidth = metadata.maxWidth,
                maxHeight = metadata.maxHeight,
            )

            hatchet.d("Rendering page ${metadata.pageNumber} of $pdfFile at scale $scale")
            // ARGB (not RGB): VGLS sheets have a transparent page background — the app supplies the
            // backdrop (as on Android). ImageType.RGB would bake in a white page fill.
            PDFRenderer(document).renderImage(metadata.pageNumber, scale, ImageType.ARGB)
        }

        return DecodeResult(
            image = bufferedImage.toSkiaBitmap().asImage(),
            isSampled = false,
        )
    }

    /** Fit the page inside [maxWidth] x [maxHeight] (px), preserving aspect ratio; never upscale absurdly. */
    private fun computeScale(
        pageWidthPts: Float,
        pageHeightPts: Float,
        maxWidth: Int,
        maxHeight: Int,
    ): Float {
        if (pageWidthPts <= 0f || pageHeightPts <= 0f) return 1f
        val widthScale = maxWidth / pageWidthPts
        val heightScale = maxHeight / pageHeightPts
        return minOf(widthScale, heightScale).coerceIn(MIN_SCALE, MAX_SCALE)
    }

    private fun BufferedImage.toSkiaBitmap(): Bitmap {
        val argb = IntArray(width * height)
        getRGB(0, 0, width, height, argb, 0, width)

        val bytes = ByteArray(width * height * BYTES_PER_PIXEL)
        var offset = 0
        for (pixel in argb) {
            // getRGB gives straight (non-premultiplied) ARGB; skia BGRA_8888 PREMUL wants byte order
            // B, G, R, A with the colour channels premultiplied by alpha. Preserving the real alpha
            // keeps the page background transparent instead of baking a colour in.
            val a = (pixel ushr 24) and 0xFF
            val r = (pixel ushr 16) and 0xFF
            val g = (pixel ushr 8) and 0xFF
            val b = pixel and 0xFF
            bytes[offset++] = (b * a / 0xFF).toByte()
            bytes[offset++] = (g * a / 0xFF).toByte()
            bytes[offset++] = (r * a / 0xFF).toByte()
            bytes[offset++] = a.toByte()
        }

        val imageInfo = ImageInfo(width, height, ColorType.BGRA_8888, ColorAlphaType.PREMUL)

        // Coil/Compose renders the wrapped image via Image.makeFromBitmap, which needs a bitmap that
        // OWNS immutable pixel memory. installPixels(...) only points the bitmap at this transient JVM
        // byte array, so makeFromBitmap fails. Instead: makeRaster copies the bytes into an immutable
        // skia Image, then readPixels copies them into a bitmap-owned allocation.
        val rasterImage = Image.makeRaster(imageInfo, bytes, width * BYTES_PER_PIXEL)
        return Bitmap().apply {
            allocPixels(imageInfo)
            rasterImage.readPixels(this)
            setImmutable()
        }
    }

    class Factory(
        private val hatchet: Hatchet,
    ) : Decoder.Factory {
        override fun create(
            result: SourceFetchResult,
            options: Options,
            imageLoader: ImageLoader,
        ) = PdfBoxImageDecoder(
            hatchet,
            result,
            options,
        )
    }

    companion object {
        private const val BYTES_PER_PIXEL = 4
        private const val MIN_SCALE = 0.1f
        private const val MAX_SCALE = 8f
    }
}
