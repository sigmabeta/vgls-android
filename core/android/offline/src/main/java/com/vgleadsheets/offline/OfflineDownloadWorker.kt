package com.vgleadsheets.offline

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class OfflineDownloadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val offlineDownloader: OfflineDownloader,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        offlineDownloader.checkAll()
        return Result.success()
    }
}
