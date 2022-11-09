package com.vgleadsheets.database.android.di

import android.content.Context
import androidx.room.Room
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
    fun provideVglsDatabase(context: Context) = Room
        .databaseBuilder(
            context,
            VglsDatabase::class.java,
            "vgls-database"
        )
        .fallbackToDestructiveMigration()
        .build()

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
    fun jamDao(
        database: VglsDatabase
    ) = database.jamDao()

    @Provides
    @Singleton
    fun setlistEntryDao(
        database: VglsDatabase
    ) = database.setlistEntryDao()

    @Provides
    @Singleton
    fun songDao(
        database: VglsDatabase
    ) = database.songDao()

    @Provides
    @Singleton
    fun songHistoryEntryDao(
        database: VglsDatabase
    ) = database.songHistoryEntryDao()

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