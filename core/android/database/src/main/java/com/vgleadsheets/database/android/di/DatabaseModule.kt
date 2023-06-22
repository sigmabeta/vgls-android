package com.vgleadsheets.database.android.di

import android.content.Context
import androidx.room.Room
import com.vgleadsheets.database.android.DatabaseVersions
import com.vgleadsheets.database.android.Migrations
import com.vgleadsheets.database.android.VglsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun providesTransactionRunner(
        database: VglsDatabase
    ) = database.transactionDao()

    @Singleton
    @Provides
    @Suppress("SpreadOperator")
    fun provideVglsDatabase(context: Context): VglsDatabase {
        return Room
            .databaseBuilder(
                context,
                VglsDatabase::class.java,
                "vgls-database"
            )
            .addMigrations(
                Migrations.RemoveJams,
                Migrations.AddFavorites,
                Migrations.AddAlternates,
            )
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
}
