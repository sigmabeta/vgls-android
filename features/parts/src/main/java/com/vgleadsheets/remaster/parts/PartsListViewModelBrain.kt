package com.vgleadsheets.remaster.parts

import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreen
import net.sigmabeta.sage.appcomm.VglsAction
import net.sigmabeta.sage.appcomm.VglsEvent
import net.sigmabeta.sage.list.ListViewModelBrain
import net.sigmabeta.sage.list.VglsScheduler
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.model.Part
import com.vgleadsheets.settings.part.SelectedPartManager
import net.sigmabeta.sage.ui.StringProvider
import kotlinx.coroutines.flow.onEach

class PartsListViewModelBrain(
    stringProvider: StringProvider,
    hatchet: Hatchet,
    private val analytics: Analytics,
    private val scheduler: VglsScheduler,
    private val selectedPartManager: SelectedPartManager,
) : ListViewModelBrain(
    stringProvider,
    analytics,
    hatchet,
    scheduler,
) {
    override val screenIdentifier = AnalyticsScreen.PART_PICKER

    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> collectSelectedPart()
            is VglsAction.Resume -> return
            is Action.PartSelected -> onPartSelected(action.option)
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
        emitEvent(VglsEvent.NavigateBack(this.javaClass.simpleName))
    }
}
