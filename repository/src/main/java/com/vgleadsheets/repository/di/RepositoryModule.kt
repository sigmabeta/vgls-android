package com.vgleadsheets.repository.di

import android.content.Context
import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.model.time.ThreeTenTime
import com.vgleadsheets.network.GiantBombApi
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.repository.RealRepository
import com.vgleadsheets.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        vglsApi: VglsApi,
        giantBombApi: GiantBombApi,
        database: VglsDatabase,
        threeTenTime: ThreeTenTime,
        @Named("VglsImageUrl") baseImageUrl: String
    ): Repository = RealRepository(vglsApi, giantBombApi, baseImageUrl, threeTenTime, database)

    @Provides
    @Singleton
    fun provideTime(context: Context): ThreeTenTime = ThreeTenTime.Impl(context)
}
