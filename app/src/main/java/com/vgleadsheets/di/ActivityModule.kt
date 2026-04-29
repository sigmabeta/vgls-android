package com.vgleadsheets.di

import android.app.Activity
import android.content.Context
import com.vgleadsheets.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appinfo.AppInfo
import net.sigmabeta.sage.coroutines.VglsDispatchers
import com.vgleadsheets.features.FeatureDirectory
import com.vgleadsheets.list.BrainProvider
import com.vgleadsheets.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.offline.OfflineDownloader
import com.vgleadsheets.offline.OfflineWorkScheduler
import com.vgleadsheets.offline.WorkManagerOfflineWorkScheduler
import com.vgleadsheets.remaster.home.HomeModuleProvider
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.DbUpdater
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.OfflineRepository
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.repository.history.UserContentGenerator
import com.vgleadsheets.repository.history.UserContentMigrator
import com.vgleadsheets.settings.DebugSettingsManager
import com.vgleadsheets.settings.GeneralSettingsManager
import com.vgleadsheets.settings.part.SelectedPartManager
import net.sigmabeta.sage.time.ThreeTenTime
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfoProvider
import net.sigmabeta.sage.wakelocks.WakeLockManager
import com.vgleadsheets.wakelocks.WakeLockManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Provides
    @ActivityScoped
    @Suppress("LongParameterList")
    fun provideVMBrainProvider(
        dispatchers: VglsDispatchers,
        delayManager: DelayManager,
        appInfo: AppInfo,
        urlInfoProvider: UrlInfoProvider,
        stringProvider: StringProvider,
        hatchet: Hatchet,
        selectedPartManager: SelectedPartManager,
        songRepository: SongRepository,
        gameRepository: GameRepository,
        composerRepository: ComposerRepository,
        randomRepository: RandomRepository,
        favoriteRepository: FavoriteRepository,
        offlineRepository: OfflineRepository,
        tagRepository: TagRepository,
        homeModuleProvider: HomeModuleProvider,
        generalSettingsManager: GeneralSettingsManager,
        debugSettingsManager: DebugSettingsManager,
        userContentGenerator: UserContentGenerator,
        userContentMigrator: UserContentMigrator,
        threeTenTime: ThreeTenTime,
        analytics: Analytics,
        dbUpdater: DbUpdater,
        songHistoryRepository: SongHistoryRepository,
        offlineDownloader: OfflineDownloader,
        offlineWorkScheduler: OfflineWorkScheduler,
    ): BrainProvider = FeatureDirectory(
            dbUpdater = dbUpdater,
            songHistoryRepository = songHistoryRepository,
            songRepository = songRepository,
            gameRepository = gameRepository,
            composerRepository = composerRepository,
            randomRepository = randomRepository,
            favoriteRepository = favoriteRepository,
            offlineRepository = offlineRepository,
            tagRepository = tagRepository,
            dispatchers = dispatchers,
            delayManager = delayManager,
            appInfo = appInfo,
            urlInfoProvider = urlInfoProvider,
            analytics = analytics,
            stringProvider = stringProvider,
            hatchet = hatchet,
            threeTenTime = threeTenTime,
            selectedPartManager = selectedPartManager,
            generalSettingsManager = generalSettingsManager,
            debugSettingsManager = debugSettingsManager,
            userContentGenerator = userContentGenerator,
            userContentMigrator = userContentMigrator,
            homeModuleProvider = homeModuleProvider,
            offlineWorkScheduler = offlineWorkScheduler,
        )

    @Provides
    @ActivityScoped
    fun provideOfflineWorkScheduler(
        @ActivityContext context: Context,
    ): OfflineWorkScheduler = WorkManagerOfflineWorkScheduler(context)

    @Provides
    @ActivityScoped
    fun provideWakeLockManager(
        @ActivityContext context: Context,
        eventDispatcher: EventDispatcher,
        stringProvider: StringProvider,
        coroutineScope: CoroutineScope,
        dispatchers: VglsDispatchers,
    ): WakeLockManager = WakeLockManagerImpl(
        context as Activity,
        eventDispatcher,
        stringProvider,
        coroutineScope,
        dispatchers,
    )
}
