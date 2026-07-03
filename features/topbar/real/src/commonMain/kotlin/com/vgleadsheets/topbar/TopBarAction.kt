package com.vgleadsheets.topbar

import net.sigmabeta.sage.appcomm.SageAction

sealed class TopBarAction : SageAction() {
    data object Menu : TopBarAction()
    data object OpenPartPicker : TopBarAction()
}
