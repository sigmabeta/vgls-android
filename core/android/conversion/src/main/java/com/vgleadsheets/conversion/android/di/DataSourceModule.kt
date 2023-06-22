package com.vgleadsheets.conversion.android.di

import com.vgleadsheets.conversion.android.converter.ComposerAliasConverter
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.android.converter.GameAliasConverter
import com.vgleadsheets.conversion.android.converter.GameConverter
import com.vgleadsheets.conversion.android.converter.SongAliasConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.android.converter.TagKeyConverter
import com.vgleadsheets.conversion.android.converter.TagValueConverter
import com.vgleadsheets.conversion.android.datasource.ComposerAliasAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.ComposerAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.DbStatisticsAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.GameAliasAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.GameAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SongAliasAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SongAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.TagKeyAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.TagValueAndroidDataSource
import com.vgleadsheets.database.android.dao.ComposerAliasRoomDao
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.dao.DbStatisticsRoomDao
import com.vgleadsheets.database.android.dao.GameAliasRoomDao
import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.android.dao.SongAliasRoomDao
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
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataSourceModule {
    @Provides
    @Singleton
    fun composerAliasDataSource(
        convert: ComposerAliasConverter,
        roomImpl: ComposerAliasRoomDao,
        otoRelatedRoomImpl: ComposerRoomDao,
        composerConverter: ComposerConverter,
    ): ComposerAliasDataSource = ComposerAliasAndroidDataSource(
        convert,
        roomImpl,
        otoRelatedRoomImpl,
        composerConverter,
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
}
