package com.vgleadsheets.jvm.di

import com.vgleadsheets.downloader.FakeSheetDownloader
import com.vgleadsheets.downloader.RealSheetDownloader
import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.downloader.StorageDirectoryProvider
import com.vgleadsheets.jvm.JvmOfflineWorkScheduler
import com.vgleadsheets.jvm.JvmWakeLockManager
import com.vgleadsheets.offline.OfflineDownloader
import com.vgleadsheets.offline.OfflineWorkScheduler
import com.vgleadsheets.remaster.home.HomeModuleProvider
import com.vgleadsheets.remaster.home.modules.MostPlaysComposerModule
import com.vgleadsheets.remaster.home.modules.MostPlaysGamesModule
import com.vgleadsheets.remaster.home.modules.MostPlaysSongsModule
import com.vgleadsheets.remaster.home.modules.MostPlaysTagValuesModule
import com.vgleadsheets.remaster.home.modules.MostSongsComposersModule
import com.vgleadsheets.remaster.home.modules.MostSongsGamesModule
import com.vgleadsheets.remaster.home.modules.NeverPlayedSongModule
import com.vgleadsheets.remaster.home.modules.NotifModule
import com.vgleadsheets.remaster.home.modules.RecentSongsModule
import com.vgleadsheets.remaster.home.modules.RngModule
import com.vgleadsheets.repository.OfflineRepository
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.wakelocks.WakeLockManager
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.perf.NoopBackend
import net.sigmabeta.sage.perf.PerfBackend
import net.sigmabeta.sage.perf.PerfMeasurer
import net.sigmabeta.sage.perf.PerfMeasurerImpl
import net.sigmabeta.sage.time.TimeProvider
import okio.Path.Companion.toOkioPath
import java.io.File

/**
 * Desktop mirror of the android app's data/offline/perf/home bindings — everything that's
 * platform-agnostic (or has a trivial desktop analog). The android app spreads these across
 * OfflineModule / DownloaderModule / PerfModule / HomeModuleModule / ActivityScopedBindingsModule;
 * they're gathered here. Image/Coil bindings are intentionally omitted (see JvmNetworkModule).
 */
@BindingContainer
@ContributesTo(AppScope::class)
object JvmDataModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideSheetDownloader(
        @Named("VglsPdfUrl") baseUrl: String?,
        fakeSheetDownloader: FakeSheetDownloader,
        realSheetDownloader: RealSheetDownloader,
    ): SheetDownloader = if (baseUrl != null) {
        realSheetDownloader
    } else {
        fakeSheetDownloader
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideStorageDirProvider(
        @Named("workDir") workDir: File,
    ): StorageDirectoryProvider = object : StorageDirectoryProvider {
        override fun getStorageDirectory() = File(workDir, "files").apply { mkdirs() }.toOkioPath()
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideOfflineDownloader(
        offlineRepository: OfflineRepository,
        sheetDownloader: SheetDownloader,
        threeTenTime: TimeProvider,
        hatchet: Hatchet,
        updateManager: UpdateManager,
    ): OfflineDownloader = OfflineDownloader(
        offlineRepo = offlineRepository,
        sheetDownloader = sheetDownloader,
        threeTenTime = threeTenTime,
        hatchet = hatchet,
        updateManager = updateManager,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun providePerfBackend(): PerfBackend = NoopBackend()

    @Provides
    @SingleIn(AppScope::class)
    fun providePerfMeasurer(
        backend: PerfBackend,
        dispatchers: SageDispatchers,
    ): PerfMeasurer = PerfMeasurerImpl(backend, dispatchers)

    @Provides
    @SingleIn(AppScope::class)
    fun provideWakeLockManager(): WakeLockManager = JvmWakeLockManager()

    @Provides
    @SingleIn(AppScope::class)
    fun provideOfflineWorkScheduler(): OfflineWorkScheduler = JvmOfflineWorkScheduler()

    @Provides
    @SingleIn(AppScope::class)
    @Suppress("LongParameterList")
    fun provideHomeModuleProvider(
        notifModule: NotifModule,
        mostSongsGamesModule: MostSongsGamesModule,
        mostSongsComposersModule: MostSongsComposersModule,
        neverPlayedSongModule: NeverPlayedSongModule,
        mostPlaysTagValuesModule: MostPlaysTagValuesModule,
        mostPlaysGamesModule: MostPlaysGamesModule,
        mostPlaysComposerModule: MostPlaysComposerModule,
        mostPlaysSongsModule: MostPlaysSongsModule,
        recentSongsModule: RecentSongsModule,
        rngModule: RngModule,
        dispatchers: SageDispatchers,
        coroutineScope: CoroutineScope,
    ): HomeModuleProvider = object : HomeModuleProvider {
        override val modules by lazy {
            val list = listOf(
                notifModule,
                neverPlayedSongModule,
                mostSongsGamesModule,
                mostSongsComposersModule,
                recentSongsModule,
                mostPlaysTagValuesModule,
                mostPlaysSongsModule,
                mostPlaysGamesModule,
                mostPlaysComposerModule,
                rngModule,
            )

            list.forEach {
                it.setup()
                    .flowOn(dispatchers.disk)
                    .launchIn(coroutineScope)
            }
            list
        }
    }
}
