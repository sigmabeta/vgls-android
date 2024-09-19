package com.vgleadsheets.conversion.android.di

import com.vgleadsheets.conversion.android.converter.ComposerAliasConverter
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.android.converter.ComposerPlayCountConverter
import com.vgleadsheets.conversion.android.converter.FavoriteComposerConverter
import com.vgleadsheets.conversion.android.converter.FavoriteGameConverter
import com.vgleadsheets.conversion.android.converter.FavoriteSongConverter
import com.vgleadsheets.conversion.android.converter.GameAliasConverter
import com.vgleadsheets.conversion.android.converter.GameConverter
import com.vgleadsheets.conversion.android.converter.GamePlayCountConverter
import com.vgleadsheets.conversion.android.converter.SearchHistoryConverter
import com.vgleadsheets.conversion.android.converter.SongAliasConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.android.converter.SongHistoryConverter
import com.vgleadsheets.conversion.android.converter.SongPlayCountConverter
import com.vgleadsheets.conversion.android.converter.TagKeyConverter
import com.vgleadsheets.conversion.android.converter.TagValueConverter
import com.vgleadsheets.conversion.android.converter.TagValuePlayCountConverter
import com.vgleadsheets.conversion.android.datasource.AlternateSettingAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.ComposerAliasAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.ComposerAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.ComposerPlayCountAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.DbStatisticsAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.FavoriteComposerAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.FavoriteGameAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.FavoriteSongAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.GameAliasAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.GameAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.GamePlayCountAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SearchHistoryAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SongAliasAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SongAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SongHistoryAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.SongPlayCountAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.TagKeyAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.TagValueAndroidDataSource
import com.vgleadsheets.conversion.android.datasource.TagValuePlayCountAndroidDataSource
import com.vgleadsheets.database.android.dao.AlternateSettingRoomDao
import com.vgleadsheets.database.android.dao.ComposerAliasRoomDao
import com.vgleadsheets.database.android.dao.ComposerPlayCountRoomDao
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.dao.DbStatisticsRoomDao
import com.vgleadsheets.database.android.dao.FavoriteComposerRoomDao
import com.vgleadsheets.database.android.dao.FavoriteGameRoomDao
import com.vgleadsheets.database.android.dao.FavoriteSongRoomDao
import com.vgleadsheets.database.android.dao.GameAliasRoomDao
import com.vgleadsheets.database.android.dao.GamePlayCountRoomDao
import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.android.dao.SearchHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongAliasRoomDao
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongPlayCountRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.dao.TagKeyRoomDao
import com.vgleadsheets.database.android.dao.TagValuePlayCountRoomDao
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
    fun tagValuePlayCountDataSource(
        convert: TagValuePlayCountConverter,
        roomImpl: TagValuePlayCountRoomDao,
    ): TagValuePlayCountDataSource = TagValuePlayCountAndroidDataSource(
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

    @Provides
    @Singleton
    fun searchHistoryDataSource(
        convert: SearchHistoryConverter,
        roomImpl: SearchHistoryEntryRoomDao,
    ): SearchHistoryDataSource = SearchHistoryAndroidDataSource(
        roomImpl,
        convert,
    )

    @Provides
    @Singleton
    fun favoriteSongDataSource(
        roomImpl: FavoriteSongRoomDao,
        converter: FavoriteSongConverter
    ): FavoriteSongDataSource = FavoriteSongAndroidDataSource(
        roomImpl,
        converter,
    )

    @Provides
    @Singleton
    fun favoriteGameDataSource(
        roomImpl: FavoriteGameRoomDao,
        converter: FavoriteGameConverter,
    ): FavoriteGameDataSource = FavoriteGameAndroidDataSource(
        roomImpl,
        converter,
    )

    @Provides
    @Singleton
    fun favoriteComposerDataSource(
        roomImpl: FavoriteComposerRoomDao,
        converter: FavoriteComposerConverter,
    ): FavoriteComposerDataSource = FavoriteComposerAndroidDataSource(
        roomImpl,
        converter,
    )

    @Provides
    @Singleton
    fun alternateSettingDataSource(
        roomImpl: AlternateSettingRoomDao,
    ): AlternateSettingDataSource = AlternateSettingAndroidDataSource(
        roomImpl,
    )
}
