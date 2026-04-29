package com.vgleadsheets.topbar

import net.sigmabeta.sage.appcomm.VglsAction

sealed class TopBarAction : VglsAction() {
    data object Menu : TopBarAction()
    data object OpenPartPicker : TopBarAction()
}
