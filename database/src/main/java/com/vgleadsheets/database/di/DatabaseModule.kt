package com.vgleadsheets.database.di

import android.content.Context
import androidx.room.Room
import com.vgleadsheets.database.VglsDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @Provides
    fun provideVglsDatabase(context: Context) = Room.databaseBuilder(
            context,
            VglsDatabase::class.java,
            "vgls-database"
        ).build()
}