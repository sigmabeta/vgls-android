package com.vgleadsheets.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.vgleadsheets.repository.ThreeTenTime
import com.vgleadsheets.storage.Storage
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context

    @Provides
    @Singleton
    @Named("CachePath") // Oh I love that app, it lets you send money to ppl
    fun provideCachePath(context: Context) = context.cacheDir.absolutePath

    @Provides
    @Singleton
    @Named("IsDebugBuild")
    fun provideIsDebugBuild() = BuildConfig.DEBUG

    @Provides
    @Named("StethoInterceptor")
    internal fun provideStethoInterceptor(): Interceptor = StethoInterceptor()

    @Provides
    @Singleton
    fun provideTime(context: Context): ThreeTenTime = ThreeTenImpl(context)

    @Provides
    @Named("NetworkEndpoint")
    @Singleton
    internal fun provideNetworkSetting(storage: Storage): Int {
        return runBlocking {
            storage.getDebugSettingNetworkEndpoint().selectedPosition
        }
    }

    @Provides
    @Singleton
    @Named("RunningTest")
    @Suppress("FunctionOnlyReturningConstant")
    fun provideRunningTest() = false
}
