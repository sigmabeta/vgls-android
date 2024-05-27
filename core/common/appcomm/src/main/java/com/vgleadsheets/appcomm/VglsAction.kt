package com.vgleadsheets.appcomm

open class VglsAction {
    data object Noop : VglsAction()

    data object InitNoArgs : VglsAction()
    data class InitWithId(val id: Long) : VglsAction()
    data class InitWithString(val arg: String) : VglsAction()

    data object Resume : VglsAction()

    data object DeviceBack : VglsAction()

    data class SnackbarActionClicked(val action: VglsAction) : VglsAction()
    data class SnackbarDismissed(val action: VglsAction) : VglsAction()
}