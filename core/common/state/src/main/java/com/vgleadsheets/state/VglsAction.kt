package com.vgleadsheets.state

open class VglsAction {
    data object Noop : VglsAction()

    data object InitNoArgs : VglsAction()
    data class InitWithId(val id: Long) : VglsAction()
    data class InitWithString(val arg: String) : VglsAction()

    data object Resume : VglsAction()
}
