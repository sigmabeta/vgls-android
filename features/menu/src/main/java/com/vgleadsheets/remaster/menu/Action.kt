package com.vgleadsheets.remaster.menu

import net.sigmabeta.sage.appcomm.VglsAction

sealed class Action : VglsAction() {
    data object CheckUpdatesClicked : Action()
    data object ClearUsageClicked : Action()
    data object ClearSheetsClicked : Action()
    data object KeepScreenOnClicked : Action()
    data object LicensesLinkClicked : Action()
    data object WebsiteLinkClicked : Action()
    data object GiantBombClicked : Action()
    data object PrivacyLinkClicked : Action()
    data object WhatsNewClicked : Action()
    data object BuildDateClicked : Action()

    data object FakeApiClicked : Action()
    data object DebugDelayClicked : Action()
    data object DebugShowNavSnackbarsClicked : Action()
    data object DebugRenderOverlayClicked : Action()
    data object GenerateUserContentClicked : Action()
    data object GenerateUserContentLegacyClicked : Action()
    data object MigrateUserContentLegacyClicked : Action()
    data object RestartAppClicked : Action()
    data object RunOfflineDownloadClicked : Action()
    data object OfflineUpdatesClicked : Action()
}
