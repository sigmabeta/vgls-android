package com.vgleadsheets.pdf

import androidx.core.graphics.drawable.toDrawable
import coil3.ImageLoader
import coil3.asImage
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import com.vgleadsheets.logging.Hatchet

class PdfImageDecoder(
    private val hatchet: Hatchet,
    private val result: SourceFetchResult,
    private val options: Options,
) : Decoder {
    @Suppress("ReturnCount")
    override suspend fun decode(): DecodeResult? {
        println("Decoding ${result.source.file().name}")
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

        val drawable = PdfToBitmapRenderer(hatchet)
            .renderToBitmap(
                pdfFile,
                metadata.pageNumber,
                metadata.maxWidth,
                metadata.maxHeight,
                1.0f
            ).toDrawable(options.context.resources)

        return DecodeResult(
            isSampled = false,
            image = drawable.asImage(),
        )
    }

    class Factory(
        private val hatchet: Hatchet,
    ) : Decoder.Factory {
        override fun create(
            result: SourceFetchResult,
            options: Options,
            imageLoader: ImageLoader
        ) = PdfImageDecoder(
            hatchet,
            result,
            options,
        )
    }
}
