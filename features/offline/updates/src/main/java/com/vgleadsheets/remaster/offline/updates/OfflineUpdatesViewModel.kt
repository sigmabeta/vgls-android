package com.vgleadsheets.remaster.offline.updates

import androidx.lifecycle.ViewModel
import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.repository.OfflineRepository
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.time.ThreeTenTime
import net.sigmabeta.sage.ui.StringProvider

@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
class OfflineUpdatesViewModel @Inject constructor(
    override val stringProvider: StringProvider,
    override val analytics: Analytics,
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
    private val offlineRepository: OfflineRepository,
    private val threeTenTime: ThreeTenTime,
) : VglsListViewModel<State>() {
    override val screenIdentifier = VglsAnalyticsScreen.OFFLINE_UPDATES

    override fun initialState() = State()

    init {
        sendAction(SageAction.InitNoArgs)
    }

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> fetchResults()
            is SageAction.Resume -> return
            is SageAction.Noop -> return
        }
    }

    private fun fetchResults() {
        offlineRepository.getAllUpdateResults()
            .onEach { list ->
                val sorted = list.sortedByDescending { it.dateTime }
                updateState {
                    State(
                        results = LCE.Content(sorted),
                        formattedDateTimes = sorted.associate {
                            it.id to threeTenTime.longDateTimeText(it.dateTime)
                        },
                        formattedServerTimes = sorted.associate {
                            it.id to threeTenTime.longDateTimeText(it.serverUpdateTime)
                        },
                    )
                }
            }
            .catch { error -> updateState { State(results = LCE.Error("fetchResults", error)) } }
            .runInBackground()
    }
}
