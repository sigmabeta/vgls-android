package com.vgleadsheets.remaster.menu

import com.vgleadsheets.model.SheetColorMode
import com.vgleadsheets.model.ThemeMode
import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class DropdownExpandClicked(val settingId: String) : Action()
    data class ThemeModeSelected(val mode: ThemeMode) : Action()
    data class SheetColorModeSelected(val mode: SheetColorMode) : Action()
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
