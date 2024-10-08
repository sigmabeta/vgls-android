package com.vgleadsheets.remaster.updates

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.ui.StringProvider

class UpdatesViewModelBrain(
    stringProvider: StringProvider,
    hatchet: Hatchet,
    private val scheduler: VglsScheduler,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    scheduler,
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> fetchUpdates()
            is VglsAction.Resume -> return
            is VglsAction.Noop -> return
        }
    }

    private fun fetchUpdates() {
        TODO("Not yet implemented")
    }
}
