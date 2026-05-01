package com.vgleadsheets.di.images

import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.pdf.subsample.PdfSubsampleSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.RenderOverlayProvider
import net.sigmabeta.sage.logging.Hatchet

@Module
@InstallIn(SingletonComponent::class)
class PdfModule {
    @Provides
    internal fun providePdfSubsampleSourceFactory(
        hatchet: Hatchet,
        sheetDownloader: SheetDownloader,
        sageDispatchers: SageDispatchers,
        renderOverlayProvider: RenderOverlayProvider,
    ) = PdfSubsampleSource.Factory(
        hatchet = hatchet,
        sheetDownloader = sheetDownloader,
        sageDispatchers = sageDispatchers,
        renderOverlayProvider = renderOverlayProvider,
    )
}
