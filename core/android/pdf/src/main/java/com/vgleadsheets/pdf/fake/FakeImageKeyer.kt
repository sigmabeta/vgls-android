package com.vgleadsheets.pdf.fake

import coil3.key.Keyer
import coil3.request.Options
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.pdf.computeWidth
import com.vgleadsheets.pdf.fakeCacheKey
import com.vgleadsheets.urlinfo.UrlInfoProvider

class FakePdfImageKeyer(
    private val urlInfoProvider: UrlInfoProvider,
) : Keyer<PdfConfigById> {
    override fun key(data: PdfConfigById, options: Options): String {
        val width = computeWidth(options)
        val cacheKey = data.fakeCacheKey(
            width,
            urlInfoProvider.urlInfoFlow.value.partId ?: throw IllegalStateException("No part selected.")
        )
        println("PDF cache key: $cacheKey")
        return cacheKey
    }
}
