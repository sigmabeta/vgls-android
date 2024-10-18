package com.vgleadsheets.di

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.vgleadsheets.BuildConfig
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventDispatcherReal
import com.vgleadsheets.appcomm.di.ActionDeserializer
import com.vgleadsheets.appinfo.AppInfo
import com.vgleadsheets.common.debug.ShowDebugProvider
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.dispatchers.DelayManagerImpl
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.notif.NotifState
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.settings.DebugSettingsManager
import com.vgleadsheets.settings.GeneralSettingsManager
import com.vgleadsheets.settings.environment.EnvironmentManager
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.storage.common.Storage
import com.vgleadsheets.time.ThreeTenTime
import com.vgleadsheets.urlinfo.UrlInfoProvider
import com.vgleadsheets.versions.AppVersionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    @Named("CachePath") // Oh I love that app, it lets you send money to ppl
    fun provideCachePath(@ApplicationContext context: Context): String = context.cacheDir.absolutePath

    @Provides
    @Singleton
    fun provideAppInfo() = AppInfo(
        isDebug = BuildConfig.DEBUG,
        versionName = BuildConfig.VERSION_NAME,
        versionCode = BuildConfig.VERSION_CODE,
        buildTimeMs = BuildConfig.BUILD_TIME,
        buildBranch = BuildConfig.BUILD_BRANCH,
    )

    @Provides
    @Singleton
    fun provideShowDebugProvider(
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: VglsDispatchers,
    ) = ShowDebugProvider(
        debugSettingsManager,
        coroutineScope,
        dispatchers,
    )

    @Provides
    @Singleton
    fun provideTime(@ApplicationContext context: Context): ThreeTenTime = ThreeTenImpl(
        context,
    )

    @Provides
    @Singleton
    @Named(NotifManager.DEP_NAME_JSON_ADAPTER_NOTIF)
    fun provideNotifJsonAdapter(moshi: Moshi): JsonAdapter<NotifState> = moshi.adapter(NotifState::class.java)

    @Provides
    @Singleton
    fun provideNotifManager(
        storage: Storage,
        @Named(NotifManager.DEP_NAME_JSON_ADAPTER_NOTIF) jsonAdapter: JsonAdapter<NotifState>,
        coroutineScope: CoroutineScope,
        dispatchers: VglsDispatchers,
        hatchet: Hatchet,
    ) = NotifManager(
        storage = storage,
        notifStateJsonAdapter = jsonAdapter,
        coroutineScope = coroutineScope,
        dispatchers = dispatchers,
        hatchet = hatchet,
    )

    @Provides
    @Singleton
    fun provideAppVersionManager(
        storage: Storage,
        updateManager: UpdateManager,
        notifManager: NotifManager,
        actionDeserializer: ActionDeserializer,
        coroutineScope: CoroutineScope,
        dispatchers: VglsDispatchers,
        hatchet: Hatchet,
    ) = AppVersionManager(
        storage = storage,
        updateManager = updateManager,
        notifManager = notifManager,
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
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: VglsDispatchers
    ) = UrlInfoProvider(
        environmentManager,
        partManager,
        debugSettingsManager,
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
