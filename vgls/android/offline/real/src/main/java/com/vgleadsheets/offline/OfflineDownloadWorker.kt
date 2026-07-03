package com.vgleadsheets.offline

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * Instantiated by `VglsWorkerFactory` (in `:app`), which pulls [offlineDownloader] off the Metro
 * `VglsAppGraph` and passes it here — the Metro replacement for the old `@HiltWorker` assisted setup.
 */
class OfflineDownloadWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val offlineDownloader: OfflineDownloader,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        offlineDownloader.checkAll()
        return Result.success()
    }
}
