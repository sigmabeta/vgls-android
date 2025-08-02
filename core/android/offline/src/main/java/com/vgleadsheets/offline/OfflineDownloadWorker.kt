package com.vgleadsheets.offline

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted

@HiltWorker
class OfflineDownloadWorker(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val offlineDownloader: OfflineDownloader,
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        offlineDownloader.checkAll()
        return Result.success()
    }
}
