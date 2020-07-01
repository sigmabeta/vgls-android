package com.vgleadsheets.di

import com.vgleadsheets.network.MockVglsApi
import com.vgleadsheets.network.StringGenerator
import com.vgleadsheets.network.VglsApi
import dagger.Module
import dagger.Provides
import java.util.Random
import javax.inject.Named
import javax.inject.Singleton

@Module
class MockApiModule {
    @Provides
    @Singleton
    fun provideVglsApi(
        random: Random,
        @Named("RngSeed") seed: Long,
        stringGenerator: StringGenerator
    ): VglsApi = MockVglsApi(random, seed, stringGenerator)
}
