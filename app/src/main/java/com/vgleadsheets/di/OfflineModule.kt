package com.vgleadsheets.di

import com.vgleadsheets.downloader.SheetDownloader
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.offline.OfflineDownloader
import com.vgleadsheets.repository.OfflineRepository
import com.vgleadsheets.repository.UpdateManager
import net.sigmabeta.sage.time.ThreeTenTime
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object OfflineModule {
    @Provides
    @Singleton
    fun provideOfflineDownloader(
        offlineRepository: OfflineRepository,
        sheetDownloader: SheetDownloader,
        threeTenTime: ThreeTenTime,
        hatchet: Hatchet,
        updateManager: UpdateManager,
    ): OfflineDownloader = OfflineDownloader(
        offlineRepo = offlineRepository,
        sheetDownloader = sheetDownloader,
        threeTenTime = threeTenTime,
        hatchet = hatchet,
        updateManager = updateManager,
    )
}
