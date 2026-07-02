package com.vgleadsheets.database.android.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.vgleadsheets.database.BuildConfig
import com.vgleadsheets.database.android.DatabaseVersions
import com.vgleadsheets.database.android.Migrations
import com.vgleadsheets.database.android.UserContentDatabase
import com.vgleadsheets.database.android.UserContentMigrations
import com.vgleadsheets.database.android.VglsDatabase
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
import com.vgleadsheets.database.android.dao.OfflineComposerRoomDao
import com.vgleadsheets.database.android.dao.OfflineGameRoomDao
import com.vgleadsheets.database.android.dao.OfflineSongRoomDao
import com.vgleadsheets.database.android.dao.OfflineUpdateRoomDao
import com.vgleadsheets.database.android.dao.SearchHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongAliasRoomDao
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongPlayCountRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.dao.TagKeyRoomDao
import com.vgleadsheets.database.android.dao.TagValuePlayCountRoomDao
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.android.dao.TransactionDao
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import net.sigmabeta.sage.di.AppScope

@BindingContainer
@ContributesTo(AppScope::class)
@Suppress("TooManyFunctions")
object DatabaseModule {
    @SingleIn(AppScope::class)
    @Provides
    fun providesTransactionRunner(
        database: VglsDatabase
    ): TransactionDao = database.transactionDao()

    @SingleIn(AppScope::class)
    @Provides
    fun provideSqlOpenHelperFactory(): SupportSQLiteOpenHelper.Factory = if (BuildConfig.DEBUG) {
        FrameworkSQLiteOpenHelperFactory()
    } else {
        RequerySQLiteOpenHelperFactory()
    }

    @SingleIn(AppScope::class)
    @Provides
    @Suppress("SpreadOperator")
    fun provideVglsDatabase(
        context: Context,
        sqlOpenHelperFactory: SupportSQLiteOpenHelper.Factory
    ): VglsDatabase = Room
            .databaseBuilder(
                context,
                VglsDatabase::class.java,
                "vgls-database"
            )
            .openHelperFactory(sqlOpenHelperFactory)
            .addMigrations(
                Migrations.RemoveJams,
                Migrations.AddFavorites,
                Migrations.AddAlternates,
                Migrations.AddSongCounts,
                Migrations.AddOfflineUpdateResults,
                Migrations.AddSongModifiedTimes,
            )
            .fallbackToDestructiveMigrationFrom(dropAllTables = true, *DatabaseVersions.WITHOUT_MIGRATION)
            .build()

    @SingleIn(AppScope::class)
    @Provides
    fun provideUserContentDatabase(
        context: Context,
        sqlOpenHelperFactory: SupportSQLiteOpenHelper.Factory
    ): UserContentDatabase = Room
            .databaseBuilder(
                context,
                UserContentDatabase::class.java,
                "user-content-database"
            )
            .addMigrations(
                UserContentMigrations.AddedOfflineSongs,
                UserContentMigrations.AddedOfflineComposers,
                UserContentMigrations.AddedOfflineGames,
            )
            .openHelperFactory(sqlOpenHelperFactory)
            .build()

    @Provides
    @SingleIn(AppScope::class)
    fun composerAliasDao(
        database: VglsDatabase
    ): ComposerAliasRoomDao = database.composerAliasDao()

    @Provides
    @SingleIn(AppScope::class)
    fun composerDao(
        database: VglsDatabase
    ): ComposerRoomDao = database.composerDao()

    @Provides
    @SingleIn(AppScope::class)
    fun dbStatisticsDao(
        database: VglsDatabase
    ): DbStatisticsRoomDao = database.dbStatisticsDao()

    @Provides
    @SingleIn(AppScope::class)
    fun gameAliasDao(
        database: VglsDatabase
    ): GameAliasRoomDao = database.gameAliasDao()

    @Provides
    @SingleIn(AppScope::class)
    fun gameDao(
        database: VglsDatabase
    ): GameRoomDao = database.gameDao()

    @Provides
    @SingleIn(AppScope::class)
    fun offlineUpdateDao(
        database: VglsDatabase
    ): OfflineUpdateRoomDao = database.offlineUpdateDao()

    @Provides
    @SingleIn(AppScope::class)
    fun songDao(
        database: VglsDatabase
    ): SongRoomDao = database.songDao()

    @Provides
    @SingleIn(AppScope::class)
    fun songAliasDao(
        database: VglsDatabase
    ): SongAliasRoomDao = database.songAliasDao()

    @Provides
    @SingleIn(AppScope::class)
    fun tagKeyDao(
        database: VglsDatabase
    ): TagKeyRoomDao = database.tagKeyDao()

    @Provides
    @SingleIn(AppScope::class)
    fun tagValueDao(
        database: VglsDatabase
    ): TagValueRoomDao = database.tagValueDao()

    @Provides
    @SingleIn(AppScope::class)
    fun songHistoryEntryDao(
        database: UserContentDatabase
    ): SongHistoryEntryRoomDao = database.songHistoryEntryDao()

    @Provides
    @SingleIn(AppScope::class)
    fun gamePlayCountDao(
        database: UserContentDatabase
    ): GamePlayCountRoomDao = database.gamePlayCountDao()

    @Provides
    @SingleIn(AppScope::class)
    fun composerPlayCountDao(
        database: UserContentDatabase
    ): ComposerPlayCountRoomDao = database.composerPlayCountDao()

    @Provides
    @SingleIn(AppScope::class)
    fun tagValuePlayCountDao(
        database: UserContentDatabase
    ): TagValuePlayCountRoomDao = database.tagValuePlayCountDao()

    @Provides
    @SingleIn(AppScope::class)
    fun songPlayCountDao(
        database: UserContentDatabase
    ): SongPlayCountRoomDao = database.songPlayCountDao()

    @Provides
    @SingleIn(AppScope::class)
    fun searchHistoryDao(
        database: UserContentDatabase
    ): SearchHistoryEntryRoomDao = database.searchHistoryDao()

    @Provides
    @SingleIn(AppScope::class)
    fun favoriteSongDao(
        database: UserContentDatabase
    ): FavoriteSongRoomDao = database.favoriteSongDao()

    @Provides
    @SingleIn(AppScope::class)
    fun favoriteGameDao(
        database: UserContentDatabase
    ): FavoriteGameRoomDao = database.favoriteGameDao()

    @Provides
    @SingleIn(AppScope::class)
    fun favoriteComposerDao(
        database: UserContentDatabase
    ): FavoriteComposerRoomDao = database.favoriteComposerDao()

    @Provides
    @SingleIn(AppScope::class)
    fun offlineSongDao(
        database: UserContentDatabase
    ): OfflineSongRoomDao = database.offlineSongDao()

    @Provides
    @SingleIn(AppScope::class)
    fun offlineComposerDao(
        database: UserContentDatabase
    ): OfflineComposerRoomDao = database.offlineComposerDao()

    @Provides
    @SingleIn(AppScope::class)
    fun offlineGameDao(
        database: UserContentDatabase
    ): OfflineGameRoomDao = database.offlineGameDao()

    @Provides
    @SingleIn(AppScope::class)
    fun alternateSettingDao(
        database: UserContentDatabase
    ): AlternateSettingRoomDao = database.alternateSettingDao()
}
