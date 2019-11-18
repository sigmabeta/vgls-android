package com.vgleadsheets.database.di

import android.content.Context
import androidx.room.Room
import com.vgleadsheets.database.VglsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
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
}
