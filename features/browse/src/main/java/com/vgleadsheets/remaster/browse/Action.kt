package com.vgleadsheets.remaster.browse

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class DestinationClicked(val destination: String) : Action()
}
