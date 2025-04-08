package com.vgleadsheets.appcomm

open class VglsAction {
    data object Noop : VglsAction()

    data object InitNoArgs : VglsAction()
    data class InitWithId(val id: Long) : VglsAction()
    data class InitWithString(val arg: String) : VglsAction()
    data class InitWithPageNumber(val id: Long, val pageNumber: Long) : VglsAction()

    data object Resume : VglsAction()
    data object Pause : VglsAction()

    data object DeviceBack : VglsAction()
    data object AppBack : VglsAction()

    data class SnackbarActionClicked(val action: VglsAction) : VglsAction()
    data class SnackbarDismissed(val action: VglsAction) : VglsAction()

    data object RefreshDbClicked : VglsAction()

    data class SearchQueryEntered(val query: String) : VglsAction()
    data object SearchClearClicked : VglsAction()

    data class NotifClearClicked(val id: Long) : VglsAction()
    data object DbSeeWhatsNewClicked : VglsAction()
    data object AppSeeWhatsNewClicked : VglsAction()

    data object KeepScreenOnSnackCtaClicked : VglsAction()

    data object PageClicked : VglsAction()
    data object PageZoomedOutMax : VglsAction()
    data class PageDoubleClicked(val pageNumber: Int) : VglsAction()
}
