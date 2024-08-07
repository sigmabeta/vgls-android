package com.vgleadsheets.remaster.menu

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data object KeepScreenOnClicked : Action()
    data object LicensesLinkClicked : Action()
    data object WebsiteLinkClicked : Action()
}
