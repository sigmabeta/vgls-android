package com.vgleadsheets.remaster.home

import com.vgleadsheets.list.ListAction
import com.vgleadsheets.list.ListViewModelBrain

class HomeViewModelBrain: ListViewModelBrain() {
    override fun initialState() = State

    override fun handleAction(action: ListAction) {
        if (action is ListAction.InitNoArgs) {
            return
        }
        throw IllegalArgumentException("Invalid action for this screen.")
    }
}
