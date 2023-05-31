package com.vgleadsheets.di

import com.vgleadsheets.network.FakeVglsApi
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
    ): VglsApi = FakeVglsApi(random, seed, stringGenerator)
}
