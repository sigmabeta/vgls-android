package com.vgleadsheets.di.images

import com.vgleadsheets.common.debug.RenderOverlayProvider
import net.sigmabeta.sage.coroutines.VglsDispatchers
import com.vgleadsheets.downloader.SheetDownloader
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.pdf.subsample.PdfSubsampleSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class PdfModule {
    @Provides
    internal fun providePdfSubsampleSourceFactory(
        hatchet: Hatchet,
        sheetDownloader: SheetDownloader,
        vglsDispatchers: VglsDispatchers,
        renderOverlayProvider: RenderOverlayProvider,
    ) = PdfSubsampleSource.Factory(
        hatchet = hatchet,
        sheetDownloader = sheetDownloader,
        vglsDispatchers = vglsDispatchers,
        renderOverlayProvider = renderOverlayProvider,
    )
}
