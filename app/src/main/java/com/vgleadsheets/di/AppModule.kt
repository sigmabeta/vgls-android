package com.vgleadsheets.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.vgleadsheets.BuildConfig
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.repository.ThreeTenTime
import com.vgleadsheets.settings.common.Storage
import com.vgleadsheets.settings.environment.EnvironmentManager
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    @Named("CachePath") // Oh I love that app, it lets you send money to ppl
    fun provideCachePath(@ApplicationContext context: Context) = context.cacheDir.absolutePath

    @Provides
    @Singleton
    @Named("IsDebugBuild")
    fun provideIsDebugBuild() = BuildConfig.DEBUG

    @Provides
    @Named("StethoInterceptor")
    internal fun provideStethoInterceptor(): Interceptor = StethoInterceptor()

    @Provides
    @Singleton
    fun provideTime(@ApplicationContext context: Context): ThreeTenTime = ThreeTenImpl(context)

    @Provides
    @Named("NetworkEndpoint")
    @Singleton
    internal fun provideNetworkSetting(storage: com.vgleadsheets.storage.Storage): Int {
        return runBlocking {
            storage.getDebugSettingNetworkEndpoint().selectedPosition
        }
    }

    // TODO this should only happen in debug builds; in release builds it should be a no-op
    @Provides
    @Singleton
    internal fun provideEnvironmentManager(
        storage: Storage
    ): EnvironmentManager {
        return EnvironmentManager(
            storage = storage
        )
    }

    @Provides
    @Singleton
    internal fun provideSelectedPartManager(
        storage: Storage
    ): SelectedPartManager {
        return SelectedPartManager(
            storage = storage
        )
    }

    @Provides
    @Singleton
    internal fun provideConverterFactory() = MoshiConverterFactory.create()

    @Provides
    @Singleton
    internal fun provideUrlInfoProvider(
        environmentManager: EnvironmentManager,
        partManager: SelectedPartManager,
        coroutineScope: CoroutineScope,
        dispatchers: VglsDispatchers
    ) = UrlInfoProvider(
        environmentManager,
        partManager,
        coroutineScope,
        dispatchers,
    )

    @Provides
    @Singleton
    @Named("RunningTest")
    @Suppress("FunctionOnlyReturningConstant")
    fun provideRunningTest() = false
}
