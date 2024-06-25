package com.vgleadsheets.repository.di

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
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.DbUpdater
import com.vgleadsheets.repository.DelayOrErrorRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.RealRepository
import com.vgleadsheets.repository.ThreeTenTime
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.repository.VglsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        realRepository: RealRepository
    ): VglsRepository = DelayOrErrorRepository(realRepository)

    @Provides
    @Singleton
    @Suppress("LongParameterList")
    fun provideRealRepository(
        dispatchers: VglsDispatchers,
        composerDataSource: ComposerDataSource,
        gameDataSource: GameDataSource,
        songDataSource: SongDataSource,
        tagKeyDataSource: TagKeyDataSource,
        tagValueDataSource: TagValueDataSource,
        songAliasDataSource: SongAliasDataSource
    ) = RealRepository(
        dispatchers,
        gameDataSource,
        composerDataSource,
        songDataSource,
        songAliasDataSource,
        tagKeyDataSource,
        tagValueDataSource,
    )

    @Provides
    @Singleton
    fun provideGameRepository(
        dispatchers: VglsDispatchers,
        gameDataSource: GameDataSource,
        gameAliasDataSource: GameAliasDataSource,
    ) = GameRepository(
        dispatchers,
        gameAliasDataSource,
        gameDataSource,
    )

    @Provides
    @Singleton
    fun provideComposerRepository(
        dispatchers: VglsDispatchers,
        composerDataSource: ComposerDataSource,
        composerAliasDataSource: ComposerAliasDataSource,
    ) = ComposerRepository(
        dispatchers,
        composerAliasDataSource,
        composerDataSource,
    )

    @Provides
    @Singleton
    @Suppress("LongParameterList")
    fun provideUpdateManager(
        vglsApi: VglsApi,
        dbUpdater: DbUpdater,
        threeTenTime: ThreeTenTime,
        dispatchers: VglsDispatchers,
        hatchet: Hatchet,
        dbStatisticsDataSource: DbStatisticsDataSource,
        coroutineScope: CoroutineScope,
        notifManager: NotifManager,
    ) = UpdateManager(
        vglsApi,
        dbUpdater,
        dbStatisticsDataSource,
        threeTenTime,
        hatchet,
        dispatchers,
        coroutineScope,
        notifManager,
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
}
