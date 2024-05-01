package com.vgleadsheets.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.vgleadsheets.BuildConfig
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.environment.EnvironmentDataStore
import com.vgleadsheets.environment.EnvironmentManager
import com.vgleadsheets.repository.ThreeTenTime
import com.vgleadsheets.storage.Storage
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
    internal fun provideNetworkSetting(storage: Storage): Int {
        return runBlocking {
            storage.getDebugSettingNetworkEndpoint().selectedPosition
        }
    }

    private val Context.debugDataStore by preferencesDataStore(name = "debug")

    @Provides
    @Named("DebugDataStore")
    @Singleton
    internal fun provideDebugDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.debugDataStore
    }

    // TODO this should only happen in debug builds; in release builds it should be a no-op
    @Provides
    @Named("EnvironmentDataStore")
    @Singleton
    internal fun provideEnvironmentManager(
        @Named("DebugDataStore") dataStore: DataStore<Preferences>,
        coroutineScope: CoroutineScope,
        dispatchers: VglsDispatchers
    ): EnvironmentManager {
        return EnvironmentDataStore(
            dataStore = dataStore,
            coroutineScope = coroutineScope,
            dispatchers = dispatchers,
        )
    }

    @Provides
    @Singleton
    @Named("RunningTest")
    @Suppress("FunctionOnlyReturningConstant")
    fun provideRunningTest() = false
}
