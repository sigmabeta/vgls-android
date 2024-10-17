package com.vgleadsheets.pdf.fake

import android.graphics.Bitmap
import coil3.ImageLoader
import coil3.asImage
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import com.vgleadsheets.bitmaps.FakePdfImageGenerator
import com.vgleadsheets.pdf.PdfMetadata
import com.vgleadsheets.pdf.computeWidth

class FakePdfImageDecoder(
    private val generator: FakePdfImageGenerator,
    private val result: SourceFetchResult,
    private val options: Options,
) : Decoder {
    @Suppress("ReturnCount")
    override suspend fun decode(): DecodeResult? {
        // Check the source is actually a pdf
        if (result.mimeType != MIMETYPE) {
            return null
        }

        val source = result.source

        val metadata = source.metadata
        if (metadata !is PdfMetadata) {
            return null
        }

        val width = computeWidth(options)

        val pdfFile = source.file().toFile()
        val path = pdfFile.path
        val pathSplit = path.split("/")
        val relevantPart = pathSplit[pathSplit.size - 2]
        val relevantPartSplit = relevantPart.split(" - ")
        val title = relevantPartSplit.last().trim()
        val gameName = relevantPartSplit.first().trim()

        val bitmap = generator.generateLoadingSheet(
            width!!,
            title,
            gameName,
            listOf("Composer One")
        )

        return DecodeResult(
            image = bitmap
                .copy(
                    Bitmap.Config.ARGB_8888,
                    false
                )
                .asImage(),
            isSampled = true,
        )
    }

    class Factory(
        private val generator: FakePdfImageGenerator,
    ) : Decoder.Factory {
        override fun create(
            result: SourceFetchResult,
            options: Options,
            imageLoader: ImageLoader
        ) = FakePdfImageDecoder(
            generator,
            result,
            options,
        )
    }

    companion object {
        private const val WIDTH_ARBITRARY = 320
        const val MIMETYPE = "application/pdf"
    }
}
