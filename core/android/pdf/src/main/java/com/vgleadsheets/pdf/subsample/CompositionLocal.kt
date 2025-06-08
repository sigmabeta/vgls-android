package com.vgleadsheets.pdf.subsample

import androidx.compose.runtime.staticCompositionLocalOf
import com.vgleadsheets.pdf.PdfConfigById

val LocalPdfSubsampler = staticCompositionLocalOf<PdfSubsampleSourceFactory> {
     object : PdfSubsampleSourceFactory {
         override fun create(data: PdfConfigById): PdfSubsampleSource {
             TODO("Not yet implemented")
         }
     }
 }
