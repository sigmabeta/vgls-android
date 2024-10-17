package com.vgleadsheets.pdf

import androidx.core.graphics.drawable.toDrawable
import coil3.ImageLoader
import coil3.asImage
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.fetch.SourceFetchResult
import coil3.request.Options

class PdfImageDecoder(
    private val pdfToBitmapRenderer: PdfToBitmapRenderer,
    private val result: SourceFetchResult,
    private val options: Options,
) : Decoder {
    @Suppress("ReturnCount")
    override suspend fun decode(): DecodeResult? {
        // Check the source is actually a pdf
        if (result.mimeType != PdfImageFetcher.MIMETYPE) {
            return null
        }

        val source = result.source

        val metadata = source.metadata
        if (metadata !is PdfMetadata) {
            return null
        }

        val pdfFile = source.file().toFile()
        val width = computeWidth(options)

        val drawable = pdfToBitmapRenderer
            .renderPdfToBitmap(pdfFile, metadata.pageNumber, width)
            .toDrawable(options.context.resources)

        return DecodeResult(
            isSampled = true,
            image = drawable.asImage(),
        )
    }

    class Factory(
        private val pdfToBitmapRenderer: PdfToBitmapRenderer,
    ) : Decoder.Factory {
        override fun create(
            result: SourceFetchResult,
            options: Options,
            imageLoader: ImageLoader
        ) = PdfImageDecoder(
            pdfToBitmapRenderer,
            result,
            options,
        )
    }
}
