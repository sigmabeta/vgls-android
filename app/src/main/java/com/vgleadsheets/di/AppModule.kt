package com.vgleadsheets.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.vgleadsheets.BuildConfig
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventDispatcherReal
import com.vgleadsheets.appcomm.di.ActionDeserializer
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.dispatchers.DelayManagerImpl
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.notif.NotifState
import com.vgleadsheets.repository.ThreeTenTime
import com.vgleadsheets.settings.DebugSettingsManager
import com.vgleadsheets.settings.GeneralSettingsManager
import com.vgleadsheets.settings.environment.EnvironmentManager
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.storage.common.Storage
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import okhttp3.Interceptor

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    @Named("CachePath") // Oh I love that app, it lets you send money to ppl
    fun provideCachePath(@ApplicationContext context: Context): String = context.cacheDir.absolutePath

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
    @Singleton
    @Named(NotifManager.DEP_NAME_JSON_ADAPTER_NOTIF)
    fun provideNotifJsonAdapter(moshi: Moshi): JsonAdapter<NotifState> = moshi.adapter(NotifState::class.java)

    @Provides
    @Singleton
    fun provideNotifManager(
        storage: Storage,
        @Named(NotifManager.DEP_NAME_JSON_ADAPTER_NOTIF) jsonAdapter: JsonAdapter<NotifState>,
        actionDeserializer: ActionDeserializer,
        coroutineScope: CoroutineScope,
        dispatchers: VglsDispatchers,
        hatchet: Hatchet,
    ) = NotifManager(
        storage = storage,
        notifStateJsonAdapter = jsonAdapter,
        actionDeserializer = actionDeserializer,
        coroutineScope = coroutineScope,
        dispatchers = dispatchers,
        hatchet = hatchet,
    )

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
    internal fun provideGeneralSettingsManager(
        storage: Storage
    ): GeneralSettingsManager {
        return GeneralSettingsManager(
            storage = storage
        )
    }

    @Provides
    @Singleton
    internal fun provideDebugSettingsManager(
        storage: Storage
    ): DebugSettingsManager {
        return DebugSettingsManager(
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
    internal fun provideDispatcherConfigProvider(
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: VglsDispatchers,
    ): DelayManager = DelayManagerImpl(
        debugSettingsManager,
        dispatchers,
        coroutineScope,
    )

    @Provides
    @Singleton
    internal fun provideEventDispatcher(): EventDispatcher = EventDispatcherReal()

    @Provides
    @Singleton
    @Named("RunningTest")
    @Suppress("FunctionOnlyReturningConstant")
    fun provideRunningTest() = false
}
