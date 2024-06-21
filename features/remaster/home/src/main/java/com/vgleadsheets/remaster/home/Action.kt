package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class MostSongsGameclicked(val gameId: Long) : Action()
}
