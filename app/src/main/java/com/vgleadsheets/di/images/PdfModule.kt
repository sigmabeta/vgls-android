package com.vgleadsheets.di.images

import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.pdf.subsample.PdfSubsampleSource
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.RenderOverlayProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet

@BindingContainer
@ContributesTo(AppScope::class)
object PdfModule {
    @Provides
    fun providePdfSubsampleSourceFactory(
        hatchet: Hatchet,
        sheetDownloader: SheetDownloader,
        sageDispatchers: SageDispatchers,
        renderOverlayProvider: RenderOverlayProvider,
    ): PdfSubsampleSource.Factory = PdfSubsampleSource.Factory(
        hatchet = hatchet,
        sheetDownloader = sheetDownloader,
        sageDispatchers = sageDispatchers,
        renderOverlayProvider = renderOverlayProvider,
    )
}
