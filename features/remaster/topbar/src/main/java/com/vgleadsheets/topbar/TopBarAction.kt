package com.vgleadsheets.topbar

import com.vgleadsheets.appcomm.VglsAction

sealed class TopBarAction : VglsAction() {
    data object Menu : TopBarAction()
    data object OpenPartPicker : TopBarAction()
    data object AppBack : TopBarAction()
}
