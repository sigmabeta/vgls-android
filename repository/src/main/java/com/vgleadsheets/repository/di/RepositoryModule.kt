package com.vgleadsheets.repository.di

import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.repository.RealRepository
import com.vgleadsheets.repository.Repository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideRepository(vglsApi: VglsApi): Repository = RealRepository(vglsApi)
}