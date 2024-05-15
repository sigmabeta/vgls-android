package com.vgleadsheets.remaster.home

import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.state.VglsAction

class HomeViewModelBrain : ListViewModelBrain() {
    override fun initialState() = State

    override fun handleAction(action: VglsAction) {
        if (action is VglsAction.InitNoArgs) {
            return
        }
        throw IllegalArgumentException("Invalid action for this screen.")
    }
}
