package com.vgleadsheets.ui.viewer

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.model.Song

sealed class Action : VglsAction() {
    data object ScreenClicked : Action()
    data object PrevButtonClicked : Action()
    data object NextButtonClicked : Action()

    data class PageClicked(val pageNumber: Int) : Action()
    data class InitWithPageNumber(val id: Long, val pageNumber: Long) : Action()
    data class SongLoaded(val song: Song) : Action()
}
