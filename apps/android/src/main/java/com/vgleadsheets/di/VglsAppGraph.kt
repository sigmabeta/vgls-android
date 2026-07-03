package com.vgleadsheets.di

import android.app.Application
import android.content.Context
import com.vgleadsheets.offline.OfflineDownloader
import com.vgleadsheets.pdf.subsample.PdfSubsampleSource
import com.vgleadsheets.remaster.ActivityDependencyInitializer
import com.vgleadsheets.wakelocks.WakeLockManagerImpl
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

/**
 * Application-wide Metro dependency graph. Owns every `AppScope`-scoped binding — both for
 * `metroViewModel<T>()` resolution (via the inherited [ViewModelGraph] multibindings) and for the
 * hand-rolled reads the `Application` / `Activity` entry points make. Replaces Hilt's
 * `@HiltAndroidApp` component (see [VglsApplication]).
 *
 * The accessors below are the ones pulled at entry points; everything else is resolved through
 * constructor injection / the VM multibinding maps and never needs to be named here.
 */
@SingleIn(AppScope::class)
@DependencyGraph(AppScope::class)
interface VglsAppGraph : ViewModelGraph {
    // Read by RemasteredActivity (via the ActivityGraph interface VglsApplication implements).
    val hatchet: Hatchet
    val activityDependencyInitializer: ActivityDependencyInitializer
    val pdfSubsampleSourceFactory: PdfSubsampleSource.Factory

    // Provided as LocalVglsStringProvider at the Compose root (Android + Desktop mirror).
    val stringProvider: StringProvider

    // Concrete impl so VglsApplication can bind/unbind the current Activity for window flags.
    val wakeLockManager: WakeLockManagerImpl

    // Pulled by the WorkManager WorkerFactory in VglsApplication.
    val offlineDownloader: OfflineDownloader

    // Bind the Application's Context unqualified — the single-scope graph has no @ApplicationContext
    // qualifier (dropped in the Hilt→Metro migration); contributed modules that take a Context get it here.
    @Provides
    @SingleIn(AppScope::class)
    fun provideAppContext(application: Application): Context = application

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): VglsAppGraph
    }
}
