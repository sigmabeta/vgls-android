package com.vgleadsheets.repository.di

import com.vgleadsheets.appcomm.di.ActionDeserializer
import com.vgleadsheets.coroutines.VglsDispatchers
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
import com.vgleadsheets.database.source.SearchHistoryDataSource
import com.vgleadsheets.database.source.SongHistoryDataSource
import com.vgleadsheets.database.source.SongPlayCountDataSource
import com.vgleadsheets.database.source.TagValuePlayCountDataSource
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.DbUpdater
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.SearchRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.repository.history.UserContentGenerator
import com.vgleadsheets.repository.history.UserContentMigrator
import com.vgleadsheets.settings.GeneralSettingsManager
import com.vgleadsheets.time.ThreeTenTime
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideSongRepository(
        songDataSource: SongDataSource,
        songAliasDataSource: SongAliasDataSource,
        alternateSettingDataSource: AlternateSettingDataSource,
    ) = SongRepository(
        songDataSource,
        songAliasDataSource,
        alternateSettingDataSource,
    )

    @Provides
    @Singleton
    fun provideGameRepository(
        gameDataSource: GameDataSource,
    ) = GameRepository(
        gameDataSource,
    )

    @Provides
    @Singleton
    fun provideComposerRepository(
        composerDataSource: ComposerDataSource,
    ) = ComposerRepository(
        composerDataSource,
    )

    @Provides
    @Singleton
    fun provideRandomRepository(
        songDataSource: SongDataSource,
        gameDataSource: GameDataSource,
        composerDataSource: ComposerDataSource,
        hatchet: Hatchet,
    ) = RandomRepository(
        songDataSource,
        composerDataSource,
        gameDataSource,
        hatchet,
    )

    @Provides
    @Singleton
    fun provideSearchRepository(
        searchHistoryDataSource: SearchHistoryDataSource,
        songDataSource: SongDataSource,
        songAliasDataSource: SongAliasDataSource,
        gameDataSource: GameDataSource,
        gameAliasDataSource: GameAliasDataSource,
        composerDataSource: ComposerDataSource,
        composerAliasDataSource: ComposerAliasDataSource,
    ) = SearchRepository(
        searchHistoryDataSource,
        songDataSource,
        songAliasDataSource,
        gameDataSource,
        gameAliasDataSource,
        composerDataSource,
        composerAliasDataSource,
    )

    @Provides
    @Singleton
    @Suppress("LongParameterList")
    fun provideUpdateManager(
        vglsApi: VglsApi,
        dbUpdater: DbUpdater,
        threeTenTime: ThreeTenTime,
        dispatchers: VglsDispatchers,
        actionDeserializer: ActionDeserializer,
        hatchet: Hatchet,
        dbStatisticsDataSource: DbStatisticsDataSource,
        coroutineScope: CoroutineScope,
        notifManager: NotifManager,
        userContentMigrator: UserContentMigrator,
    ) = UpdateManager(
        vglsApi,
        dbUpdater,
        dbStatisticsDataSource,
        threeTenTime,
        actionDeserializer,
        hatchet,
        dispatchers,
        coroutineScope,
        notifManager,
        userContentMigrator,
    )

    @Provides
    @Singleton
    @Suppress("LongParameterList")
    fun provideDbUpdater(
        vglsApi: VglsApi,
        transactionDao: TransactionDao,
        threeTenTime: ThreeTenTime,
        dispatchers: VglsDispatchers,
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
    ) = DbUpdater(
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
    @Singleton
    @Suppress("LongParameterList")
    fun provideSongHistoryRepository(
        dispatchers: VglsDispatchers,
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
    ) = SongHistoryRepository(
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
    @Singleton
    fun provideFavoriteRepository(
        songDataSource: SongDataSource,
        gameDataSource: GameDataSource,
        composerDataSource: ComposerDataSource,
        favoriteSongDataSource: FavoriteSongDataSource,
        favoriteGameDataSource: FavoriteGameDataSource,
        favoriteComposerDataSource: FavoriteComposerDataSource,
    ) = FavoriteRepository(
        songDataSource,
        gameDataSource,
        composerDataSource,
        favoriteSongDataSource,
        favoriteGameDataSource,
        favoriteComposerDataSource,
    )

    @Provides
    @Singleton
    fun provideTagRepository(
        tagKeyDataSource: TagKeyDataSource,
        tagValueDataSource: TagValueDataSource,
    ) = TagRepository(
        tagKeyDataSource,
        tagValueDataSource,
    )

    @Provides
    @Singleton
    fun providesUserContentGenerator(
        songHistoryRepository: SongHistoryRepository,
        songDataSource: SongDataSource,
        hatchet: Hatchet
    ) = UserContentGenerator(
        songHistoryRepository = songHistoryRepository,
        songDataSource = songDataSource,
        hatchet = hatchet,
    )

    @Provides
    @Singleton
    fun providesUserContentMigrator(
        songHistoryRepository: SongHistoryRepository,
        songDataSource: SongDataSource,
        settingsManager: GeneralSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: VglsDispatchers,
        hatchet: Hatchet
    ) = UserContentMigrator(
        songHistoryRepository = songHistoryRepository,
        songDataSource = songDataSource,
        settingsManager = settingsManager,
        hatchet = hatchet,
        coroutineScope = coroutineScope,
        dispatchers = dispatchers,
    )
}
