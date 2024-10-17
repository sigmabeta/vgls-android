package com.vgleadsheets.di.network

import com.vgleadsheets.network.FakeModelGenerator
import com.vgleadsheets.network.FakeSheetDownloadApi
import com.vgleadsheets.network.FakeVglsApi
import com.vgleadsheets.network.SheetDownloadApi
import com.vgleadsheets.network.VglsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ApiModule::class]
)
object FakeApiModule {
    @Provides
    @Singleton
    fun provideVglsApi(
        fakeModelGenerator: FakeModelGenerator,
    ): VglsApi = FakeVglsApi(fakeModelGenerator)

    @Provides
    @Singleton
    fun provideFakeSheetDownloadApi(): SheetDownloadApi {
        return FakeSheetDownloadApi()
    }
}
