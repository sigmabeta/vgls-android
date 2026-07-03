package com.vgleadsheets.jvm

import com.vgleadsheets.offline.OfflineWorkScheduler

/**
 * Desktop no-op. The android impl enqueues WorkManager jobs to download sheets for offline use in the
 * background; the desktop has no WorkManager and no offline mode yet, so scheduling is a no-op.
 */
class JvmOfflineWorkScheduler : OfflineWorkScheduler {
    override fun scheduleDownload() = Unit

    override fun schedulePeriodicDownload() = Unit
}
