package com.vgleadsheets.ui.viewer

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class PageClicked(val pageNumber: Int) : Action()
    data class InitWithPageNumber(val id: Long, val pageNumber: Long) : Action()
}
