package com.vgleadsheets.di

import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.network.FakeVglsApi
import com.vgleadsheets.network.VglsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Random
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MockApiModule {
    @Provides
    @Singleton
    fun provideVglsApi(
        random: Random,
        @Named("RngSeed") seed: Long,
        stringGenerator: StringGenerator
    ): VglsApi = FakeVglsApi(random, seed, stringGenerator)
}
