package com.vgleadsheets.remaster.menu

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data object KeepScreenOnClicked : Action()
    data object LicensesLinkClicked : Action()
    data object WebsiteLinkClicked : Action()
    data object GiantBombClicked : Action()
    data object WhatsNewClicked : Action()
    data object BuildDateClicked : Action()

    data object DebugDelayClicked : Action()
    data object DebugShowNavSnackbarsClicked : Action()
    data object GenerateUserContentClicked : Action()
    data object GenerateUserContentLegacyClicked : Action()
    data object MigrateUserContentLegacyClicked : Action()
    data object RestartAppClicked : Action()
}
