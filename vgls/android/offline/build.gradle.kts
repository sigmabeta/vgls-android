plugins {
    alias(libs.plugins.sage.android)
}

dependencies {
    implementation(projects.vgls.common.offline)
    // WorkManager runtime (OfflineDownloadWorker/WorkManagerOfflineWorkScheduler); the worker is a
    // plain CoroutineWorker instantiated by VglsWorkerFactory now — no Hilt-work integration.
    implementation(libs.androidx.work.manager)
}

android {
    namespace = "com.vgleadsheets.offline"
}
