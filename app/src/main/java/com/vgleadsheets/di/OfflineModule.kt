package com.vgleadsheets.di

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.offline.OfflineDownloader
import com.vgleadsheets.repository.OfflineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@InstallIn(SingletonComponent::class)
@Module
object OfflineModule {
    @Provides
    @Singleton
    fun provideOfflineDownloader(
        offlineRepository: OfflineRepository,
        sheetDownloader: SheetDownloader,
        hatchet: Hatchet,
        scope: CoroutineScope,
        dispatchers: VglsDispatchers
    ): OfflineDownloader = OfflineDownloader(
        offlineRepo = offlineRepository,
        sheetDownloader = sheetDownloader,
        hatchet = hatchet,
        scope = scope,
        dispatchers = dispatchers
    )
}
