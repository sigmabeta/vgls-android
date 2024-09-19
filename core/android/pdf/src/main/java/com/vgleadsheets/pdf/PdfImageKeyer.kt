package com.vgleadsheets.pdf

import coil.key.Keyer
import coil.request.Options
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.urlinfo.UrlInfoProvider
import javax.inject.Inject

class PdfImageKeyer @Inject constructor(
    private val urlInfoProvider: UrlInfoProvider,
    private val songRepository: SongRepository,
) : Keyer<PdfConfigById> {
    override fun key(data: PdfConfigById, options: Options): String {
        val width = computeWidth(options)
        return data.cacheKey(
            width,
            urlInfoProvider.urlInfoFlow.value.partId ?: throw IllegalStateException("No part selected.")
        )
    }
}
