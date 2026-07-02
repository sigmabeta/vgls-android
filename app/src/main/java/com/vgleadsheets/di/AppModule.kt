package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.BuildConfig
import com.vgleadsheets.appcomm.ActionDeserializer
import com.vgleadsheets.dispatchers.DelayManagerImpl
import com.vgleadsheets.environment.Environment
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.strings.VglsStringProvider
import com.vgleadsheets.strings.loadVglsStrings
import com.vgleadsheets.urlinfo.UrlInfoProvider
import com.vgleadsheets.versions.AppVersionManager
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
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
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.ui.StringProvider

@BindingContainer
@ContributesTo(AppScope::class)
object AppModule {
    @Provides
    @SingleIn(AppScope::class)
    // Preload the single multiplatform string source (composeResources) once at startup; the map-backed
    // VglsStringProvider then serves the synchronous StringProvider calls used off the composition.
    fun provideStringProvider(): StringProvider = runBlocking { VglsStringProvider(loadVglsStrings()) }

    @Provides
    @SingleIn(AppScope::class)
    @Named("CachePath") // Oh I love that app, it lets you send money to ppl
    fun provideCachePath(context: Context): String = context.cacheDir.absolutePath

    @Provides
    @SingleIn(AppScope::class)
    fun provideAppInfo(): AppInfo = AppInfo(
        isDebug = BuildConfig.DEBUG,
        versionName = BuildConfig.VERSION_NAME,
        versionCode = BuildConfig.VERSION_CODE,
        buildTimeMs = BuildConfig.BUILD_TIME,
        buildBranch = BuildConfig.BUILD_BRANCH,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideShowDebugProvider(
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
    ): ShowDebugProvider = ShowDebugProvider(
        debugSettingsManager,
        coroutineScope,
        dispatchers,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideRenderOverlayProvider(
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
    ): RenderOverlayProvider = RenderOverlayProvider(
        debugSettingsManager,
        coroutineScope,
        dispatchers,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideTime(context: Context): ThreeTenTime = ThreeTenImpl(
        context,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideNotifManager(
        storage: Storage,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
        hatchet: Hatchet,
    ): NotifManager = NotifManager(
        storage = storage,
        coroutineScope = coroutineScope,
        dispatchers = dispatchers,
        hatchet = hatchet,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideAppVersionManager(
        storage: Storage,
        updateManager: UpdateManager,
        notifManager: NotifManager,
        actionDeserializer: ActionDeserializer,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
        hatchet: Hatchet,
    ): AppVersionManager = AppVersionManager(
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
    @SingleIn(AppScope::class)
    fun provideEnvironmentManager(
        storage: Storage
    ): EnvironmentManager = EnvironmentManager(
            storage = storage,
            environments = Environment.entries,
            default = Environment.PROD,
        )

    @Provides
    @SingleIn(AppScope::class)
    fun provideGeneralSettingsManager(
        storage: Storage
    ): GeneralSettingsManager = GeneralSettingsManager(
            storage = storage
        )

    @Provides
    @SingleIn(AppScope::class)
    fun provideDebugSettingsManager(
        storage: Storage
    ): DebugSettingsManager = DebugSettingsManager(
            storage = storage
        )

    @Provides
    @SingleIn(AppScope::class)
    fun provideSelectedPartManager(
        storage: Storage
    ): SelectedPartManager = SelectedPartManager(
            storage = storage
        )

    @Provides
    @SingleIn(AppScope::class)
    fun provideUrlInfoProvider(
        environmentManager: EnvironmentManager,
        partManager: SelectedPartManager,
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers
    ): UrlInfoProvider = UrlInfoProvider(
        environmentManager,
        partManager,
        debugSettingsManager,
        coroutineScope,
        dispatchers,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideDispatcherConfigProvider(
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
    ): DelayManager = DelayManagerImpl(
        debugSettingsManager,
        dispatchers,
        coroutineScope,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideEventDispatcher(
        analytics: Analytics
    ): EventDispatcher = EventDispatcherReal(
        analytics = analytics,
    )

    @Provides
    @SingleIn(AppScope::class)
    @Named("RunningTest")
    @Suppress("FunctionOnlyReturningConstant")
    fun provideRunningTest(): Boolean = false
}
