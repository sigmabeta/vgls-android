package com.vgleadsheets.remaster.home

import com.vgleadsheets.state.VglsAction

sealed class Action : VglsAction() {
    data class DestinationClicked(val destination: String) : Action()
}
