package com.vgleadsheets.di.images

import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.logging.Hatchet
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
    ) = PdfSubsampleSource.Factory(
        hatchet = hatchet,
        sheetDownloader = sheetDownloader
    )
}
