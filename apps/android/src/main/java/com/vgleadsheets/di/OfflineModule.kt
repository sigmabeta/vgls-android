package com.vgleadsheets.di

import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.offline.OfflineDownloader
import com.vgleadsheets.repository.OfflineRepository
import com.vgleadsheets.repository.UpdateManager
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.time.TimeProvider

@BindingContainer
@ContributesTo(AppScope::class)
object OfflineModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideOfflineDownloader(
        offlineRepository: OfflineRepository,
        sheetDownloader: SheetDownloader,
        threeTenTime: TimeProvider,
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
