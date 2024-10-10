package com.vgleadsheets.ui.viewer

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data object ScreenClicked : Action()
    data object PrevButtonClicked : Action()
    data object NextButtonClicked : Action()

    data class InitWithPageNumber(val id: Long, val pageNumber: Long) : Action()
}
