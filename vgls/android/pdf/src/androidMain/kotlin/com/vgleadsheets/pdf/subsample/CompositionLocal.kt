package com.vgleadsheets.pdf.subsample

import androidx.compose.runtime.staticCompositionLocalOf
import me.saket.telephoto.subsamplingimage.SubSamplingImageSource
import net.sigmabeta.sage.pdf.PdfConfigById

val LocalPdfSubsampler = staticCompositionLocalOf<PdfSubsampleSourceFactory> {
    object : PdfSubsampleSourceFactory {
        override fun create(data: PdfConfigById): SubSamplingImageSource {
            error("LocalPdfSubsampler not provided")
        }
    }
}
