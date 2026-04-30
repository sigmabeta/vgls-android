package com.vgleadsheets.pdf.subsample

import androidx.compose.runtime.staticCompositionLocalOf
import net.sigmabeta.sage.pdf.PdfConfigById

val LocalPdfSubsampler = staticCompositionLocalOf<PdfSubsampleSourceFactory> {
     object : PdfSubsampleSourceFactory {
         override fun create(data: PdfConfigById): PdfSubsampleSource {
             TODO("Not yet implemented")
         }
     }
 }
