package com.vgleadsheets.remaster.parts

import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.model.Part
import com.vgleadsheets.settings.part.SelectedPartManager
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.list.ListViewModelBrain
import net.sigmabeta.sage.list.SageScheduler
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

class PartsListViewModelBrain(
    stringProvider: StringProvider,
    hatchet: Hatchet,
    private val analytics: Analytics,
    private val scheduler: SageScheduler,
    private val selectedPartManager: SelectedPartManager,
) : ListViewModelBrain(
    stringProvider,
    analytics,
    hatchet,
    scheduler,
) {
    override val screenIdentifier = VglsAnalyticsScreen.PART_PICKER

    override fun initialState() = State()

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> collectSelectedPart()
            is SageAction.Resume -> return
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
        emitEvent(SageEvent.NavigateBack(this.javaClass.simpleName))
    }
}
