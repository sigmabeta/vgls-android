package com.vgleadsheets.remaster.home

import com.vgleadsheets.list.ListAction

sealed class Action : ListAction() {
    data class DestinationClicked(val destination: String) : Action()
}
