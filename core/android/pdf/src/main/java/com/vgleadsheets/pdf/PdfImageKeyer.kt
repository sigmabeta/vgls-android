package com.vgleadsheets.pdf

import coil.key.Keyer
import coil.request.Options
import javax.inject.Inject

class PdfImageKeyer @Inject constructor(): Keyer<PdfConfigById> {
    override fun key(data: PdfConfigById, options: Options): String {
        val width = computeWidth(options)
        return data.cacheKey(width)
    }
}
