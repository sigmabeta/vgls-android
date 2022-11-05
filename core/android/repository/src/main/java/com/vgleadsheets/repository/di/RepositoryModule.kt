package com.vgleadsheets.repository.di

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.android.VglsDatabase
import com.vgleadsheets.database.dao.ComposerAliasDataSource
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.DbStatisticsDataSource
import com.vgleadsheets.database.dao.GameAliasDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.JamDataSource
import com.vgleadsheets.database.dao.SetlistEntryDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.dao.SongHistoryEntryDataSource
import com.vgleadsheets.database.dao.TagKeyDataSource
import com.vgleadsheets.database.dao.TagValueDataSource
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.repository.RealRepository
import com.vgleadsheets.repository.ThreeTenTime
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.tracking.Tracker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        realRepository: RealRepository
    ): VglsRepository = realRepository

    @Provides
    @Singleton
    fun provideRealRepository(
        vglsApi: VglsApi,
        database: VglsDatabase,
        threeTenTime: ThreeTenTime,
        tracker: Tracker,
        dispatchers: VglsDispatchers,
        composerAliasDataSource: ComposerAliasDataSource,
        composerDataSource: ComposerDataSource,
        dbStatisticsDataSource: DbStatisticsDataSource,
        gameAliasDataSource: GameAliasDataSource,
        gameDataSource: GameDataSource,
        jamDataSource: JamDataSource,
        setlistEntryDataSource: SetlistEntryDataSource,
        songDataSource: SongDataSource,
        songHistoryEntryDataSource: SongHistoryEntryDataSource,
        tagKeyDataSource: TagKeyDataSource,
        tagValueDataSource: TagValueDataSource
    ) = RealRepository(
        vglsApi,
        database,
        threeTenTime,
        tracker,
        dispatchers,
        composerAliasDataSource,
        composerDataSource,
        dbStatisticsDataSource,
        gameAliasDataSource,
        gameDataSource,
        jamDataSource,
        setlistEntryDataSource,
        songDataSource,
        songHistoryEntryDataSource,
        tagKeyDataSource,
        tagValueDataSource,
    )
}
