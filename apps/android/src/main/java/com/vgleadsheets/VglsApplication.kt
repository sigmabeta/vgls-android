package com.vgleadsheets

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.vgleadsheets.di.VglsAppGraph
import com.vgleadsheets.offline.OfflineDownloadWorker
import com.vgleadsheets.offline.WorkManagerOfflineWorkScheduler
import com.vgleadsheets.pdf.subsample.PdfSubsampleSource
import com.vgleadsheets.remaster.ActivityDependencyInitializer
import com.vgleadsheets.remaster.ActivityGraph
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

/**
 * Owns the application-wide Metro [VglsAppGraph] (the replacement for Hilt's `@HiltAndroidApp`).
 * Implements [ActivityGraph] so [com.vgleadsheets.remaster.RemasteredActivity] — which lives in a
 * downstream library module and can't see this class — can read what it needs via
 * `application as ActivityGraph`.
 */
class VglsApplication :
    Application(),
    Configuration.Provider,
    ActivityGraph {

    /**
     * `lazy` so the graph is built on first read rather than at process start; the factory binds this
     * Application into the graph, and Context flows from `provideAppContext`.
     */
    val appGraph: VglsAppGraph by lazy {
        createGraphFactory<VglsAppGraph.Factory>().create(this)
    }

    // --- ActivityGraph: read by RemasteredActivity via (application as ActivityGraph) ---
    override val hatchet: Hatchet get() = appGraph.hatchet
    override val activityDependencyInitializer: ActivityDependencyInitializer
        get() = appGraph.activityDependencyInitializer
    override val pdfSubsampleSourceFactory: PdfSubsampleSource.Factory
        get() = appGraph.pdfSubsampleSourceFactory
    override val metroViewModelFactory: MetroViewModelFactory get() = appGraph.metroViewModelFactory
    override val stringProvider: StringProvider get() = appGraph.stringProvider

    override fun bindActivity(activity: Activity) = appGraph.wakeLockManager.bindActivity(activity)
    override fun unbindActivity() = appGraph.wakeLockManager.bindActivity(null)

    override fun onCreate() {
        super.onCreate()
        WorkManagerOfflineWorkScheduler(this).schedulePeriodicDownload()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(VglsWorkerFactory(appGraph))
            .build()
}

/**
 * Metro replacement for Hilt's `HiltWorkerFactory`: instantiates the app's [ListenableWorker]s with
 * dependencies pulled off the [VglsAppGraph]. Add a branch per worker as they're introduced.
 */
private class VglsWorkerFactory(private val graph: VglsAppGraph) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? = when (workerClassName) {
        OfflineDownloadWorker::class.java.name ->
            OfflineDownloadWorker(appContext, workerParameters, graph.offlineDownloader)

        else -> null
    }
}
