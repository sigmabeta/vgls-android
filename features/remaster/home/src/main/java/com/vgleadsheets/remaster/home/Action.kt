package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class DestinationClicked(val destination: String) : Action()
}
