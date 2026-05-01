package com.vgleadsheets.di

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.vgleadsheets.BuildConfig
import com.vgleadsheets.dispatchers.DelayManagerImpl
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.notif.NotifState
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.urlinfo.UrlInfoProvider
import com.vgleadsheets.versions.AppVersionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.di.ActionDeserializer
import net.sigmabeta.sage.appinfo.AppInfo
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.RenderOverlayProvider
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.events.EventDispatcherReal
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.settings.DebugSettingsManager
import net.sigmabeta.sage.settings.GeneralSettingsManager
import net.sigmabeta.sage.settings.environment.EnvironmentManager
import net.sigmabeta.sage.storage.common.Storage
import net.sigmabeta.sage.time.ThreeTenTime
import net.sigmabeta.sage.ui.StringProvider
import net.sigmabeta.sage.ui.StringResources
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideStringProvider(@ApplicationContext context: Context): StringProvider = StringResources(context.resources)

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
        dispatchers: SageDispatchers,
    ) = ShowDebugProvider(
        debugSettingsManager,
        coroutineScope,
        dispatchers,
    )

    @Provides
    @Singleton
    fun provideRenderOverlayProvider(
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
    ) = RenderOverlayProvider(
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
        dispatchers: SageDispatchers,
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
        dispatchers: SageDispatchers,
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
    ): EnvironmentManager = EnvironmentManager(
            storage = storage
        )

    @Provides
    @Singleton
    internal fun provideGeneralSettingsManager(
        storage: Storage
    ): GeneralSettingsManager = GeneralSettingsManager(
            storage = storage
        )

    @Provides
    @Singleton
    internal fun provideDebugSettingsManager(
        storage: Storage
    ): DebugSettingsManager = DebugSettingsManager(
            storage = storage
        )

    @Provides
    @Singleton
    internal fun provideSelectedPartManager(
        storage: Storage
    ): SelectedPartManager = SelectedPartManager(
            storage = storage
        )

    @Provides
    @Singleton
    internal fun provideUrlInfoProvider(
        environmentManager: EnvironmentManager,
        partManager: SelectedPartManager,
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers
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
        dispatchers: SageDispatchers,
    ): DelayManager = DelayManagerImpl(
        debugSettingsManager,
        dispatchers,
        coroutineScope,
    )

    @Provides
    @Singleton
    internal fun provideEventDispatcher(
        analytics: Analytics
    ): EventDispatcher = EventDispatcherReal(
        analytics = analytics,
    )

    @Provides
    @Singleton
    @Named("RunningTest")
    @Suppress("FunctionOnlyReturningConstant")
    fun provideRunningTest() = false
}
