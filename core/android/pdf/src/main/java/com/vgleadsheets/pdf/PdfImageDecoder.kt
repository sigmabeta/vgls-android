package com.vgleadsheets.pdf

import androidx.core.graphics.drawable.toDrawable
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.DecodeResult
import coil.decode.Decoder
import coil.fetch.SourceResult
import coil.request.Options
import javax.inject.Inject

class PdfImageDecoder(
    private val pdfToBitmapRenderer: PdfToBitmapRenderer,
    private val result: SourceResult,
    private val options: Options,
) : Decoder {
    @OptIn(ExperimentalCoilApi::class)
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
            drawable = drawable,
        )
    }

    class Factory @Inject constructor(
        private val pdfToBitmapRenderer: PdfToBitmapRenderer,
    ) : Decoder.Factory {
        override fun create(
            result: SourceResult,
            options: Options,
            imageLoader: ImageLoader
        ) = PdfImageDecoder(
            pdfToBitmapRenderer,
            result,
            options,
        )
    }
}
