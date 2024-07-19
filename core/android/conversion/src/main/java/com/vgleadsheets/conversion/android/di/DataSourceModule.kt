package com.vgleadsheets.conversion.android.di

import com.vgleadsheets.conversion.android.converter.ComposerAliasConverter
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.android.converter.ComposerPlayCountConverter
import com.vgleadsheets.conversion.android.converter.GameAliasConverter
import com.vgleadsheets.conversion.android.converter.GameConverter
import com.vgleadsheets.conversion.android.converter.GamePlayCountConverter
import com.vgleadsheets.conversion.android.converter.SongAliasConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.android.converter.SongHistoryConverter
import com.vgleadsheets.conversion.android.converter.SongPlayCountConverter
import com.vgleadsheets.conversion.android.converter.TagKeyConverter
import com.vgleadsheets.conversion.android.converter.TagValueConverter
import com.vgleadsheets.conversion.android.datasource.ComposerAliasAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.ComposerAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.ComposerPlayCountAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.DbStatisticsAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.GameAliasAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.GameAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.GamePlayCountAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SongAliasAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SongAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SongHistoryAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SongPlayCountAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.TagKeyAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.TagValueAndroidDataSource
import com.vgleadsheets.database.android.dao.ComposerAliasRoomDao
import com.vgleadsheets.database.android.dao.ComposerPlayCountRoomDao
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.dao.DbStatisticsRoomDao
import com.vgleadsheets.database.android.dao.GameAliasRoomDao
import com.vgleadsheets.database.android.dao.GamePlayCountRoomDao
import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.android.dao.SongAliasRoomDao
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongPlayCountRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.dao.TagKeyRoomDao
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.dao.ComposerAliasDataSource
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.DbStatisticsDataSource
import com.vgleadsheets.database.dao.GameAliasDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.SongAliasDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.dao.TagKeyDataSource
import com.vgleadsheets.database.dao.TagValueDataSource
import com.vgleadsheets.database.source.ComposerPlayCountDataSource
import com.vgleadsheets.database.source.GamePlayCountDataSource
import com.vgleadsheets.database.source.SongHistoryDataSource
import com.vgleadsheets.database.source.SongPlayCountDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {
    @Provides
    @Singleton
    fun composerAliasDataSource(
        convert: ComposerAliasConverter,
        roomImpl: ComposerAliasRoomDao,
    ): ComposerAliasDataSource = ComposerAliasAndroidDataSource(
        convert,
        roomImpl,
    )

    @Provides
    @Singleton
    fun composerDataSource(
        convert: ComposerConverter,
        roomImpl: ComposerRoomDao,
    ): ComposerDataSource = ComposerAndroidDataSource(
        convert,
        roomImpl,
    )

    @Provides
    @Singleton
    fun dbstatsDataSource(
        roomImpl: DbStatisticsRoomDao,
    ): DbStatisticsDataSource = DbStatisticsAndroidDataSource(
        roomImpl,
    )

    @Provides
    @Singleton
    fun gameAliasDataSource(
        convert: GameAliasConverter,
        roomImpl: GameAliasRoomDao,
    ): GameAliasDataSource = GameAliasAndroidDataSource(
        convert,
        roomImpl,
    )

    @Provides
    @Singleton
    fun gameDataSource(
        convert: GameConverter,
        roomImpl: GameRoomDao,
    ): GameDataSource = GameAndroidDataSource(
        convert,
        roomImpl,
    )

    @Provides
    @Singleton
    fun songAliasDataSource(
        convert: SongAliasConverter,
        roomImpl: SongAliasRoomDao,
    ): SongAliasDataSource = SongAliasAndroidDataSource(
        convert,
        roomImpl,
    )

    @Provides
    @Singleton
    fun songDataSource(
        convert: SongConverter,
        roomImpl: SongRoomDao,
    ): SongDataSource = SongAndroidDataSource(
        convert,
        roomImpl,
    )

    @Provides
    @Singleton
    fun tagKeyDataSource(
        convert: TagKeyConverter,
        roomImpl: TagKeyRoomDao,
    ): TagKeyDataSource = TagKeyAndroidDataSource(
        convert,
        roomImpl,
    )

    @Provides
    @Singleton
    fun tagValueDataSource(
        convert: TagValueConverter,
        roomImpl: TagValueRoomDao,
    ): TagValueDataSource = TagValueAndroidDataSource(
        convert,
        roomImpl,
    )

    @Provides
    @Singleton
    fun songHistoryDataSource(
        convert: SongHistoryConverter,
        roomImpl: SongHistoryEntryRoomDao,
    ): SongHistoryDataSource = SongHistoryAndroidDataSource(
        roomImpl,
        convert,
    )

    @Provides
    @Singleton
    fun gamePlayCountDataSource(
        convert: GamePlayCountConverter,
        roomImpl: GamePlayCountRoomDao,
    ): GamePlayCountDataSource = GamePlayCountAndroidDataSource(
        roomImpl,
        convert,
    )

    @Provides
    @Singleton
    fun composerPlayCountDataSource(
        convert: ComposerPlayCountConverter,
        roomImpl: ComposerPlayCountRoomDao,
    ): ComposerPlayCountDataSource = ComposerPlayCountAndroidDataSource(
        roomImpl,
        convert,
    )

    @Provides
    @Singleton
    fun songPlayCountDataSource(
        convert: SongPlayCountConverter,
        roomImpl: SongPlayCountRoomDao,
    ): SongPlayCountDataSource = SongPlayCountAndroidDataSource(
        roomImpl,
        convert,
    )
}
