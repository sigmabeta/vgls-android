package com.vgleadsheets.repository.di

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.model.time.ThreeTenTime
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.repository.BuildConfig
import com.vgleadsheets.repository.DelayOrErrorRepository
import com.vgleadsheets.repository.RealRepository
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.tracking.Tracker
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        realRepository: RealRepository
    ): Repository = if (BuildConfig.DEBUG) {
        DelayOrErrorRepository(realRepository)
    } else {
        realRepository
    }

    @Provides
    @Singleton
    fun provideRealRepository(
        vglsApi: VglsApi,
        database: VglsDatabase,
        threeTenTime: ThreeTenTime,
        tracker: Tracker,
        dispatchers: VglsDispatchers
    ) = RealRepository(vglsApi, threeTenTime, tracker, dispatchers, database)
}
