package com.vgleadsheets.database.di

import android.content.Context
import androidx.room.Room
import com.vgleadsheets.database.VglsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MockDatabaseModule {
    @Singleton
    @Provides
    fun provideVglsDatabase(context: Context) = Room
        .inMemoryDatabaseBuilder(context, VglsDatabase::class.java)
        .build()
}
