package com.vgleadsheets.repository.di

import com.vgleadsheets.database.VglsDatabase
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
        database: VglsDatabase,
        @Named("BaseImageUrl") baseImageUrl: String
    ): Repository = RealRepository(vglsApi, baseImageUrl, database)
}
