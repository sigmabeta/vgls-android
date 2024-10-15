package com.vgleadsheets

import android.app.Application
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.repository.history.UserContentMigrator
import com.vgleadsheets.time.ThreeTenTime
import com.vgleadsheets.versions.AppVersionManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class VglsApplication :
    Application() {
    @Inject
    lateinit var updateManager: UpdateManager

    @Inject
    lateinit var hatchet: Hatchet

    @Inject
    lateinit var userContentMigrator: UserContentMigrator

    @Inject
    lateinit var appVersionManager: AppVersionManager

    @Inject
    lateinit var threeTenTime: ThreeTenTime

    override fun onCreate() {
        super.onCreate()

        threeTenTime.init()
        appVersionManager.reportAppVersion(BuildConfig.VERSION_CODE)
    }
}
