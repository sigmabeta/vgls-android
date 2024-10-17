package com.vgleadsheets.pdf.fake

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
): Decoder {
    override suspend fun decode(): DecodeResult? {
        println("Decode request (fake) for $result")
        // Check the source is actually a pdf
        if (result.mimeType != MIMETYPE) {
            return null
        }

        val source = result.source

        val metadata = source.metadata
        if (metadata !is PdfMetadata) {
            return null
        }

        val pdfFile = source.file().toFile()
        val path = pdfFile.path
        val pathSplit = path.split("/")
        val relevantPart = pathSplit[pathSplit.size - 2]
        val relevantPartSplit = relevantPart.split(" - ")
        val title = relevantPartSplit[1].trim()
        val gameName = relevantPartSplit[0].trim()

        val width = computeWidth(options)

        return DecodeResult(
            image = generator.generateLoadingSheet(
                width!!,
                title,
                gameName,
                listOf("Composer One")
            ).asImage(),
            isSampled = true,
        )
    }


    class Factory (
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
