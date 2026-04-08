package com.vgleadsheets.offline

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class WorkManagerOfflineWorkScheduler(
    private val context: Context,
) : OfflineWorkScheduler {
    override fun scheduleDownload() {
        WorkManager.getInstance(context)
            .enqueue(OneTimeWorkRequestBuilder<OfflineDownloadWorker>().build())
    }
}
