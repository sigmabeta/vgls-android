package com.vgleadsheets.repository.di

import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.repository.RealRepository
import com.vgleadsheets.repository.Repository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideRepository(vglsApi: VglsApi, database: VglsDatabase): Repository = RealRepository(vglsApi, database)
}