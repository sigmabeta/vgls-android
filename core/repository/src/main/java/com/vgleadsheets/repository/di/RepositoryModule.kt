package com.vgleadsheets.repository.di

import android.content.Context
import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.model.time.ThreeTenTime
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.repository.RealRepository
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.tracking.Tracker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        vglsApi: VglsApi,
        database: VglsDatabase,
        threeTenTime: ThreeTenTime,
        tracker: Tracker
    ): Repository = RealRepository(vglsApi, threeTenTime, tracker, database)

    @Provides
    @Singleton
    fun provideTime(context: Context): ThreeTenTime = ThreeTenTime.Impl(context)
}
