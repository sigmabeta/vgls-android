package com.vgleadsheets.repository.di

import com.vgleadsheets.appcomm.ActionDeserializer
import com.vgleadsheets.database.android.dao.TransactionDao
import com.vgleadsheets.database.dao.ComposerAliasDataSource
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.DbStatisticsDataSource
import com.vgleadsheets.database.dao.GameAliasDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.SongAliasDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.dao.TagKeyDataSource
import com.vgleadsheets.database.dao.TagValueDataSource
import com.vgleadsheets.database.source.AlternateSettingDataSource
import com.vgleadsheets.database.source.ComposerPlayCountDataSource
import com.vgleadsheets.database.source.FavoriteComposerDataSource
import com.vgleadsheets.database.source.FavoriteGameDataSource
import com.vgleadsheets.database.source.FavoriteSongDataSource
import com.vgleadsheets.database.source.GamePlayCountDataSource
import com.vgleadsheets.database.source.OfflineComposerDataSource
import com.vgleadsheets.database.source.OfflineGameDataSource
import com.vgleadsheets.database.source.OfflineSongDataSource
import com.vgleadsheets.database.source.OfflineUpdateResultDataSource
import com.vgleadsheets.database.source.SearchHistoryDataSource
import com.vgleadsheets.database.source.SongHistoryDataSource
import com.vgleadsheets.database.source.SongPlayCountDataSource
import com.vgleadsheets.database.source.TagValuePlayCountDataSource
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.DbUpdater
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.OfflineRepository
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.SearchRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.repository.history.UserContentGenerator
import com.vgleadsheets.repository.history.UserContentMigrator
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.settings.GeneralSettingsManager
import net.sigmabeta.sage.time.ThreeTenTime
import net.sigmabeta.sage.ui.StringProvider

@BindingContainer
@ContributesTo(AppScope::class)
object RepositoryModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideSongRepository(
        songDataSource: SongDataSource,
        songAliasDataSource: SongAliasDataSource,
        alternateSettingDataSource: AlternateSettingDataSource,
    ): SongRepository = SongRepository(
        songDataSource,
        songAliasDataSource,
        alternateSettingDataSource,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideGameRepository(
        gameDataSource: GameDataSource,
    ): GameRepository = GameRepository(
        gameDataSource,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideComposerRepository(
        composerDataSource: ComposerDataSource,
    ): ComposerRepository = ComposerRepository(
        composerDataSource,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideRandomRepository(
        songDataSource: SongDataSource,
        gameDataSource: GameDataSource,
        composerDataSource: ComposerDataSource,
        hatchet: Hatchet,
    ): RandomRepository = RandomRepository(
        songDataSource,
        composerDataSource,
        gameDataSource,
        hatchet,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideSearchRepository(
        searchHistoryDataSource: SearchHistoryDataSource,
        songDataSource: SongDataSource,
        songAliasDataSource: SongAliasDataSource,
        gameDataSource: GameDataSource,
        gameAliasDataSource: GameAliasDataSource,
        composerDataSource: ComposerDataSource,
        composerAliasDataSource: ComposerAliasDataSource,
    ): SearchRepository = SearchRepository(
        searchHistoryDataSource,
        songDataSource,
        songAliasDataSource,
        gameDataSource,
        gameAliasDataSource,
        composerDataSource,
        composerAliasDataSource,
    )

    @Provides
    @SingleIn(AppScope::class)
    @Suppress("LongParameterList")
    fun provideUpdateManager(
        vglsApi: VglsApi,
        dbUpdater: DbUpdater,
        threeTenTime: ThreeTenTime,
        dispatchers: SageDispatchers,
        actionDeserializer: ActionDeserializer,
        hatchet: Hatchet,
        dbStatisticsDataSource: DbStatisticsDataSource,
        coroutineScope: CoroutineScope,
        notifManager: NotifManager,
        stringProvider: StringProvider,
    ): UpdateManager = UpdateManager(
        vglsApi,
        dbUpdater,
        dbStatisticsDataSource,
        threeTenTime,
        actionDeserializer,
        hatchet,
        dispatchers,
        coroutineScope,
        notifManager,
        stringProvider,
    )

    @Provides
    @SingleIn(AppScope::class)
    @Suppress("LongParameterList")
    fun provideDbUpdater(
        vglsApi: VglsApi,
        transactionDao: TransactionDao,
        threeTenTime: ThreeTenTime,
        dispatchers: SageDispatchers,
        hatchet: Hatchet,
        composerAliasDataSource: ComposerAliasDataSource,
        composerDataSource: ComposerDataSource,
        dbStatisticsDataSource: DbStatisticsDataSource,
        gameAliasDataSource: GameAliasDataSource,
        gameDataSource: GameDataSource,
        songDataSource: SongDataSource,
        tagKeyDataSource: TagKeyDataSource,
        tagValueDataSource: TagValueDataSource,
        songAliasDataSource: SongAliasDataSource
    ): DbUpdater = DbUpdater(
        vglsApi,
        transactionDao,
        threeTenTime,
        dispatchers,
        hatchet,
        composerAliasDataSource,
        composerDataSource,
        gameAliasDataSource,
        gameDataSource,
        songDataSource,
        songAliasDataSource,
        tagKeyDataSource,
        tagValueDataSource,
        dbStatisticsDataSource,
    )

    @Provides
    @SingleIn(AppScope::class)
    @Suppress("LongParameterList")
    fun provideSongHistoryRepository(
        dispatchers: SageDispatchers,
        hatchet: Hatchet,
        songHistoryDataSource: SongHistoryDataSource,
        gamePlayCountDataSource: GamePlayCountDataSource,
        composerPlayCountDataSource: ComposerPlayCountDataSource,
        songPlayCountDataSource: SongPlayCountDataSource,
        gameDataSource: GameDataSource,
        composerDataSource: ComposerDataSource,
        songDataSource: SongDataSource,
        coroutineScope: CoroutineScope,
        tagValuePlayCountDataSource: TagValuePlayCountDataSource,
        tagValueDataSource: TagValueDataSource,
    ): SongHistoryRepository = SongHistoryRepository(
        songHistoryDataSource,
        gamePlayCountDataSource,
        composerPlayCountDataSource,
        songPlayCountDataSource,
        tagValuePlayCountDataSource,
        gameDataSource,
        composerDataSource,
        tagValueDataSource,
        songDataSource,
        coroutineScope,
        dispatchers,
        hatchet,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideFavoriteRepository(
        songDataSource: SongDataSource,
        gameDataSource: GameDataSource,
        composerDataSource: ComposerDataSource,
        favoriteSongDataSource: FavoriteSongDataSource,
        favoriteGameDataSource: FavoriteGameDataSource,
        favoriteComposerDataSource: FavoriteComposerDataSource,
    ): FavoriteRepository = FavoriteRepository(
        songDataSource,
        gameDataSource,
        composerDataSource,
        favoriteSongDataSource,
        favoriteGameDataSource,
        favoriteComposerDataSource,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideOfflineRepository(
        songDataSource: SongDataSource,
        composerDataSource: ComposerDataSource,
        gameDataSource: GameDataSource,
        offlineSongDataSource: OfflineSongDataSource,
        offlineComposerDataSource: OfflineComposerDataSource,
        offlineGameDataSource: OfflineGameDataSource,
        offlineUpdateResultDataSource: OfflineUpdateResultDataSource,
        dbStatisticsDataSource: DbStatisticsDataSource,
        threeTenTime: ThreeTenTime,
    ): OfflineRepository = OfflineRepository(
        songDataSource,
        composerDataSource,
        gameDataSource,
        offlineSongDataSource,
        offlineComposerDataSource,
        offlineGameDataSource,
        offlineUpdateResultDataSource,
        dbStatisticsDataSource,
        threeTenTime,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideTagRepository(
        tagKeyDataSource: TagKeyDataSource,
        tagValueDataSource: TagValueDataSource,
    ): TagRepository = TagRepository(
        tagKeyDataSource,
        tagValueDataSource,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun providesUserContentGenerator(
        songHistoryRepository: SongHistoryRepository,
        songDataSource: SongDataSource,
        hatchet: Hatchet
    ): UserContentGenerator = UserContentGenerator(
        songHistoryRepository = songHistoryRepository,
        songDataSource = songDataSource,
        hatchet = hatchet,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun providesUserContentMigrator(
        songHistoryRepository: SongHistoryRepository,
        songDataSource: SongDataSource,
        settingsManager: GeneralSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
        hatchet: Hatchet
    ): UserContentMigrator = UserContentMigrator(
        songHistoryRepository = songHistoryRepository,
        songDataSource = songDataSource,
        settingsManager = settingsManager,
        hatchet = hatchet,
        coroutineScope = coroutineScope,
        dispatchers = dispatchers,
    )
}
