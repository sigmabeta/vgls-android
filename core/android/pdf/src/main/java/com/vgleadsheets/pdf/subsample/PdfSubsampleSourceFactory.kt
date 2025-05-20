package com.vgleadsheets.pdf.subsample

import com.vgleadsheets.pdf.PdfConfigById

interface PdfSubsampleSourceFactory {
    fun create(data: PdfConfigById, ): PdfSubsampleSource
}

