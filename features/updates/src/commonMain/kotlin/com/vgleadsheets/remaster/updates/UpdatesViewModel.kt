package com.vgleadsheets.remaster.updates

import androidx.lifecycle.ViewModel
import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.model.updates.AppUpdate
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
class UpdatesViewModel @Inject constructor(
    override val stringProvider: StringProvider,
    override val analytics: Analytics,
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
) : VglsListViewModel<State>() {
    override val screenIdentifier = VglsAnalyticsScreen.UPDATES

    override fun initialState() = State()

    init {
        sendAction(SageAction.InitNoArgs)
    }

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> fetchUpdates()
            is SageAction.Resume -> return
            is SageAction.Noop -> return
        }
    }

    private fun fetchUpdates() {
        updateState {
            it.copy(
                updates = generateUpdates()
            )
        }
    }

    private fun generateUpdates(): LCE.Content<List<AppUpdate>> = LCE.Content(
            data = listOf(
                AppUpdate.VERSION_2_1_0,
                AppUpdate.VERSION_2_0_5,
                AppUpdate.VERSION_2_0_4,
                AppUpdate.VERSION_2_0_3,
                AppUpdate.VERSION_2_0_0,
            )
        )
}
