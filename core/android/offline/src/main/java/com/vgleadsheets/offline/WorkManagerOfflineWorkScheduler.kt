package com.vgleadsheets.offline

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkManagerOfflineWorkScheduler(
    private val context: Context,
) : OfflineWorkScheduler {
    override fun scheduleDownload() {
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<OfflineDownloadWorker>().build(),
            )
    }

    override fun schedulePeriodicDownload() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val request = PeriodicWorkRequestBuilder<OfflineDownloadWorker>(PERIOD_DAYS, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                PERIODIC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request,
            )
    }

    companion object {
        const val WORK_NAME = "offline_download"
        const val PERIODIC_WORK_NAME = "offline_download_periodic"
        private const val PERIOD_DAYS = 7L
    }
}
