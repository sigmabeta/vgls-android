package com.vgleadsheets.pdf.subsample

import me.saket.telephoto.subsamplingimage.SubSamplingImageSource
import net.sigmabeta.sage.pdf.PdfConfigById

/**
 * Builds a telephoto [SubSamplingImageSource] for a sheet PDF. The interface is commonMain (returns
 * telephoto's multiplatform source type) so the shared ZoomableSheet composable can build one; the
 * android implementation ([PdfSubsampleSource], PdfRenderer-backed) lives in androidMain. Desktop
 * supplies its own/stub factory.
 */
interface PdfSubsampleSourceFactory {
    fun create(data: PdfConfigById): SubSamplingImageSource
}
