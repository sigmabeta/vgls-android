package com.vgleadsheets.database.android.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.vgleadsheets.database.BuildConfig
import com.vgleadsheets.database.android.DatabaseVersions
import com.vgleadsheets.database.android.Migrations
import com.vgleadsheets.database.android.UserContentDatabase
import com.vgleadsheets.database.android.VglsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
@Suppress("TooManyFunctions")
object DatabaseModule {
    @Singleton
    @Provides
    fun providesTransactionRunner(
        database: VglsDatabase
    ) = database.transactionDao()

    @Singleton
    @Provides
    fun provideSqlOpenHelperFactory() = if (BuildConfig.DEBUG) {
        FrameworkSQLiteOpenHelperFactory()
    } else {
        RequerySQLiteOpenHelperFactory()
    }

    @Singleton
    @Provides
    @Suppress("SpreadOperator")
    fun provideVglsDatabase(
        @ApplicationContext context: Context,
        sqlOpenHelperFactory: SupportSQLiteOpenHelper.Factory
    ): VglsDatabase {
        return Room
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
            )
            .fallbackToDestructiveMigrationFrom(*DatabaseVersions.WITHOUT_MIGRATION)
            .build()
    }

    @Singleton
    @Provides
    @Suppress("SpreadOperator")
    fun provideUserContentDatabase(
        @ApplicationContext context: Context,
        sqlOpenHelperFactory: SupportSQLiteOpenHelper.Factory
    ): UserContentDatabase {
        return Room
            .databaseBuilder(
                context,
                UserContentDatabase::class.java,
                "user-content-database"
            )
            .openHelperFactory(sqlOpenHelperFactory)
            .fallbackToDestructiveMigrationFrom(*DatabaseVersions.WITHOUT_MIGRATION)
            .build()
    }

    @Provides
    @Singleton
    fun composerAliasDao(
        database: VglsDatabase
    ) = database.composerAliasDao()

    @Provides
    @Singleton
    fun composerDao(
        database: VglsDatabase
    ) = database.composerDao()

    @Provides
    @Singleton
    fun dbStatisticsDao(
        database: VglsDatabase
    ) = database.dbStatisticsDao()

    @Provides
    @Singleton
    fun gameAliasDao(
        database: VglsDatabase
    ) = database.gameAliasDao()

    @Provides
    @Singleton
    fun gameDao(
        database: VglsDatabase
    ) = database.gameDao()

    @Provides
    @Singleton
    fun songDao(
        database: VglsDatabase
    ) = database.songDao()

    @Provides
    @Singleton
    fun songAliasDao(
        database: VglsDatabase
    ) = database.songAliasDao()

    @Provides
    @Singleton
    fun tagKeyDao(
        database: VglsDatabase
    ) = database.tagKeyDao()

    @Provides
    @Singleton
    fun tagValueDao(
        database: VglsDatabase
    ) = database.tagValueDao()

    @Provides
    @Singleton
    fun songHistoryEntryDao(
        database: UserContentDatabase
    ) = database.songHistoryEntryDao()

    @Provides
    @Singleton
    fun gamePlayCountDao(
        database: UserContentDatabase
    ) = database.gamePlayCountDao()

    @Provides
    @Singleton
    fun composerPlayCountDao(
        database: UserContentDatabase
    ) = database.composerPlayCountDao()

    @Provides
    @Singleton
    fun tagValuePlayCountDao(
        database: UserContentDatabase
    ) = database.tagValuePlayCountDao()

    @Provides
    @Singleton
    fun songPlayCountDao(
        database: UserContentDatabase
    ) = database.songPlayCountDao()

    @Provides
    @Singleton
    fun searchHistoryDao(
        database: UserContentDatabase
    ) = database.searchHistoryDao()

    @Provides
    @Singleton
    fun favoriteSongDao(
        database: UserContentDatabase
    ) = database.favoriteSongDao()

    @Provides
    @Singleton
    fun favoriteGameDao(
        database: UserContentDatabase
    ) = database.favoriteGameDao()

    @Provides
    @Singleton
    fun favoriteComposerDao(
        database: UserContentDatabase
    ) = database.favoriteComposerDao()

    @Provides
    @Singleton
    fun alternateSettingDao(
        database: UserContentDatabase
    ) = database.alternateSettingDao()
}
