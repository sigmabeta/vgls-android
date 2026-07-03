package com.vgleadsheets.jvm.di

import com.vgleadsheets.appcomm.ActionDeserializer
import com.vgleadsheets.dispatchers.DelayManagerImpl
import com.vgleadsheets.environment.Environment
import com.vgleadsheets.jvm.JvmNetworkStatusProvider
import com.vgleadsheets.jvm.JvmStorage
import com.vgleadsheets.jvm.TimeProviderImpl
import com.vgleadsheets.jvm.logging.JvmHatchet
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appinfo.AppInfo
import net.sigmabeta.sage.connectivity.NetworkStatusProvider
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.RenderOverlayProvider
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.events.EventDispatcherReal
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.settings.DebugSettingsManager
import net.sigmabeta.sage.settings.GeneralSettingsManager
import net.sigmabeta.sage.settings.environment.EnvironmentManager
import net.sigmabeta.sage.storage.common.Storage
import net.sigmabeta.sage.time.TimeProvider
import net.sigmabeta.sage.ui.StringProvider
import java.io.File

/**
 * JVM/desktop platform bindings — the analog of the Android app's platform modules (coroutines,
 * connectivity, storage, logging) plus the app-level providers from AppModule. All AppScope.
 */
@BindingContainer
@ContributesTo(AppScope::class)
object JvmPlatformModule {
    /** Per-OS data directory under the user's home; holds the sqlite DBs + settings + pdf cache. */
    @Provides
    @SingleIn(AppScope::class)
    @Named("workDir")
    fun provideWorkDir(): File = File(System.getProperty("user.home"), ".vgleadsheets").apply { mkdirs() }

    @Provides
    @SingleIn(AppScope::class)
    @Named("CachePath")
    fun provideCachePath(@Named("workDir") workDir: File): String =
        File(workDir, "pdfs").apply { mkdirs() }.absolutePath

    @Provides
    @SingleIn(AppScope::class)
    fun provideHatchet(): Hatchet = JvmHatchet()

    @Provides
    @SingleIn(AppScope::class)
    fun provideDispatchers(): SageDispatchers = SageDispatchers(
        computation = Dispatchers.Default,
        disk = Dispatchers.IO,
        network = Dispatchers.IO,
        main = Dispatchers.Main,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideCoroutineScope(dispatchers: SageDispatchers): CoroutineScope =
        CoroutineScope(SupervisorJob() + dispatchers.computation)

    @Provides
    @SingleIn(AppScope::class)
    fun provideStorage(@Named("workDir") workDir: File): Storage =
        JvmStorage(File(workDir, "settings.properties"))

    @Provides
    @SingleIn(AppScope::class)
    fun provideNetworkStatusProvider(): NetworkStatusProvider = JvmNetworkStatusProvider()

    @Provides
    @SingleIn(AppScope::class)
    fun provideAppInfo(): AppInfo = AppInfo(
        isDebug = true,
        versionName = "desktop",
        versionCode = 1,
        buildTimeMs = 0L,
        buildBranch = "desktop",
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideTime(): TimeProvider = TimeProviderImpl()

    @Provides
    @SingleIn(AppScope::class)
    fun provideStringProvider(): StringProvider = runBlocking { VglsStringProvider(loadVglsStrings()) }

    @Provides
    @SingleIn(AppScope::class)
    @Named("RunningTest")
    @Suppress("FunctionOnlyReturningConstant")
    fun provideRunningTest(): Boolean = false
}

/**
 * The app-level managers — a straight port of the Android app's AppModule (they construct from
 * Storage / dispatchers / scope, all platform-agnostic).
 */
@BindingContainer
@ContributesTo(AppScope::class)
object JvmAppModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideShowDebugProvider(
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
    ): ShowDebugProvider = ShowDebugProvider(debugSettingsManager, coroutineScope, dispatchers)

    @Provides
    @SingleIn(AppScope::class)
    fun provideRenderOverlayProvider(
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
    ): RenderOverlayProvider = RenderOverlayProvider(debugSettingsManager, coroutineScope, dispatchers)

    @Provides
    @SingleIn(AppScope::class)
    fun provideNotifManager(
        storage: Storage,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
        hatchet: Hatchet,
    ): NotifManager = NotifManager(storage, coroutineScope, dispatchers, hatchet)

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
        storage, updateManager, notifManager, actionDeserializer, coroutineScope, dispatchers, hatchet,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideEnvironmentManager(storage: Storage): EnvironmentManager = EnvironmentManager(
        storage = storage,
        environments = Environment.entries,
        default = Environment.PROD,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideGeneralSettingsManager(storage: Storage): GeneralSettingsManager =
        GeneralSettingsManager(storage = storage)

    @Provides
    @SingleIn(AppScope::class)
    fun provideDebugSettingsManager(storage: Storage): DebugSettingsManager =
        DebugSettingsManager(storage = storage)

    @Provides
    @SingleIn(AppScope::class)
    fun provideSelectedPartManager(storage: Storage): SelectedPartManager =
        SelectedPartManager(storage = storage)

    @Provides
    @SingleIn(AppScope::class)
    fun provideUrlInfoProvider(
        environmentManager: EnvironmentManager,
        partManager: SelectedPartManager,
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
    ): UrlInfoProvider = UrlInfoProvider(
        environmentManager, partManager, debugSettingsManager, coroutineScope, dispatchers,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideDelayManager(
        debugSettingsManager: DebugSettingsManager,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
    ): DelayManager = DelayManagerImpl(debugSettingsManager, dispatchers, coroutineScope)

    @Provides
    @SingleIn(AppScope::class)
    fun provideEventDispatcher(analytics: Analytics): EventDispatcher =
        EventDispatcherReal(analytics = analytics)
}
