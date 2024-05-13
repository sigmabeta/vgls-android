package com.vgleadsheets.remaster.browse

import com.vgleadsheets.list.ListAction

sealed class Action : ListAction() {
    data class DestinationClicked(val destination: String) : Action()
}
