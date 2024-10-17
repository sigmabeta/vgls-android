package com.vgleadsheets.pdf

import coil3.key.Keyer
import coil3.request.Options
import com.vgleadsheets.urlinfo.UrlInfoProvider

class PdfImageKeyer(
    private val urlInfoProvider: UrlInfoProvider,
) : Keyer<PdfConfigById> {
    override fun key(data: PdfConfigById, options: Options): String {
        val width = computeWidth(options)
        val cacheKey = data.cacheKey(
            width,
            urlInfoProvider.urlInfoFlow.value.partId ?: throw IllegalStateException("No part selected.")
        )
        return cacheKey
    }
}
