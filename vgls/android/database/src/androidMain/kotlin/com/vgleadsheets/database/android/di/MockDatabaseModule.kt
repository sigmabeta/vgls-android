package com.vgleadsheets.database.android.di

import android.content.Context
import androidx.room.Room
import com.vgleadsheets.database.android.VglsDatabase

// To implement, see here: https://dagger.dev/hilt/testing
// @TestInstallIn(SingletonComponent::class)
// @Module
object MockDatabaseModule {
    // @Singleton
    // @Provides
    fun provideVglsDatabase(context: Context) = Room
        .inMemoryDatabaseBuilder(context, VglsDatabase::class.java)
        .build()
}
