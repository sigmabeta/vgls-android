package com.vgleadsheets.versions

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.di.ActionDeserializer
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.notif.Notif
import com.vgleadsheets.notif.NotifCategory
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.storage.common.Storage
import com.vgleadsheets.ui.StringId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AppVersionManager(
    private val storage: Storage,
    private val updateManager: UpdateManager,
    private val notifManager: NotifManager,
    private val actionDeserializer: ActionDeserializer,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
    private val hatchet: Hatchet,
) {
    fun reportAppVersion(versionCode: Int) {
        reportAppVersionInternal(versionCode)
    }

    private fun reportAppVersionInternal(currentVersion: Int) {
        coroutineScope.launch(dispatchers.disk) {
            val savedVersion = storage
                .savedIntFlow(KEY_CURRENT_VERSION)
                .firstOrNull()
                .orMaybeNot()

            compareVersions(savedVersion, currentVersion)

            storage.saveInt(KEY_CURRENT_VERSION, currentVersion)
        }
    }

    @Suppress("ReturnCount")
    private fun compareVersions(savedVersion: Int, currentVersion: Int) {
        if (savedVersion == currentVersion) {
            hatchet.v("Current version matches previous app execution.")
            return
        }

        if (savedVersion > currentVersion) {
            hatchet.e("Somehow savedVersion $savedVersion is higher than current version $currentVersion.")
            sendNotif(
                title = StringId.NOTIF_TITLE_APP_UPDATE_ERROR,
                description = "App update information formatted incorrectly. The app will probably not work right.",
                action = null,
                actionLabel = "",
                isError = true
            )
            return
        }

        if (savedVersion <= VERSION_NEVER_LAUNCHED) {
            hatchet.i("App being launched for first time.")
            sendNotif(
                title = StringId.NOTIF_TITLE_APP_UPDATE_FIRST_TIME,
                description = "Here are some suggested songs to get you started.",
                action = null,
                actionLabel = "",
                isError = false
            )
            return
        }

        if (savedVersion <= VERSION_BEFORE_REMASTER) {
            hatchet.i("Returning user, but from before the remaster.")
            sendNotif(
                title = StringId.NOTIF_TITLE_APP_UPDATE_FIRST_SINCE_REMASTER,
                description = "The app has been rewritten from the ground up to jam even harder.",
                action = VglsAction.AppSeeWhatsNewClicked,
                actionLabel = "See what's new",
                isError = false
            )
            return
        }

        hatchet.i("Returning user, launching version $currentVersion for the first time.")
        sendNotif(
            title = StringId.NOTIF_TITLE_APP_UPDATE_DEFAULT,
            description = "Here's what's changed since the last time you used the app.",
            action = VglsAction.AppSeeWhatsNewClicked,
            actionLabel = "See what's new",
            isError = false
        )
    }

    private fun sendNotif(
        title: StringId,
        description: String,
        action: VglsAction?,
        actionLabel: String,
        isError: Boolean,
    ) {
        notifManager.addNotif(
            Notif(
                id = title.hashCode().toLong(),
                title = title,
                description = description,
                actionLabel = actionLabel,
                category = if (isError) NotifCategory.ERROR else NotifCategory.APP_UPDATE,
                isOneTime = true,
                action = actionDeserializer.serializeAction(action),
            )
        )
    }

    private suspend fun Int?.orMaybeNot() = this ?: if (hasLaunchedAppBefore()) {
        VERSION_BEFORE_REMASTER
    } else {
        VERSION_NEVER_LAUNCHED
    }

    private suspend fun hasLaunchedAppBefore(): Boolean {
        val lastApiUpdateTimeMs = updateManager.getLastApiUpdateTime().firstOrNull()?.timeMs ?: 0L
        return lastApiUpdateTimeMs > 0L
    }

    companion object {
        const val KEY_CURRENT_VERSION = "AppVersionManager.CurrentVersion"

        const val VERSION_BEFORE_REMASTER = 10000
        const val VERSION_NEVER_LAUNCHED = 0
    }
}
