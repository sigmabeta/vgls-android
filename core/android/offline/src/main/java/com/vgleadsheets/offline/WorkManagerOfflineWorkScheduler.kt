package com.vgleadsheets.offline

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

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

    companion object {
        const val WORK_NAME = "offline_download"
    }
}
