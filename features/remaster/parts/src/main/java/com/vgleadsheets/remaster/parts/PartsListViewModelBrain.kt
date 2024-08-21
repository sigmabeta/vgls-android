package com.vgleadsheets.remaster.parts

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Part
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.onEach

class PartsListViewModelBrain(
    stringProvider: StringProvider,
    hatchet: Hatchet,
    private val scheduler: VglsScheduler,
    private val selectedPartManager: SelectedPartManager,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    scheduler,
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> collectSelectedPart()
            is VglsAction.Resume -> return
            is Action.PartSelected -> onPartSelected(action.option)
            else -> throw IllegalArgumentException("Invalid action for this screen.")
        }
    }

    private fun collectSelectedPart() {
        selectedPartManager.selectedPartFlow()
            .onEach(::onSelectedPartLoaded)
            .runInBackground()
    }

    private fun onSelectedPartLoaded(part: Part) {
        updateState {
            (it as State).copy(
                selectedPart = part
            )
        }
    }

    private fun onPartSelected(option: PartSelectorOption) {
        selectedPartManager.setPart(
            Part.valueOf(option.name)
        )
    }
}
