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
import com.vgleadsheets.repository.DelayOrErrorRepository
import com.vgleadsheets.repository.RealRepository
import com.vgleadsheets.repository.ThreeTenTime
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.tracking.Tracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
    fun provideRealRepository(
        vglsApi: VglsApi,
        transactionDao: TransactionDao,
        threeTenTime: ThreeTenTime,
        tracker: Tracker,
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
    ) = RealRepository(
        vglsApi,
        transactionDao,
        threeTenTime,
        tracker,
        dispatchers,
        hatchet,
        composerAliasDataSource,
        composerDataSource,
        dbStatisticsDataSource,
        gameAliasDataSource,
        gameDataSource,
        songDataSource,
        songAliasDataSource,
        tagKeyDataSource,
        tagValueDataSource,
    )
}
