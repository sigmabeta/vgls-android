package com.vgleadsheets.remaster.parts

import androidx.lifecycle.ViewModel
import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.model.Part
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
class PartsListViewModel @Inject constructor(
    override val stringProvider: StringProvider,
    override val analytics: Analytics,
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
    private val selectedPartManager: SelectedPartManager,
) : VglsListViewModel<State>() {
    override val screenIdentifier = VglsAnalyticsScreen.PART_PICKER

    override fun initialState() = State()

    init {
        sendAction(SageAction.InitNoArgs)
    }

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
            it.copy(
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
