package com.vgleadsheets.pdf.subsample

import net.sigmabeta.sage.pdf.PdfConfigById

interface PdfSubsampleSourceFactory {
    fun create(data: PdfConfigById,): PdfSubsampleSource
}
