package com.vgleadsheets.remaster.offline.updates

import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreen
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.VglsAction
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.repository.OfflineRepository
import net.sigmabeta.sage.time.ThreeTenTime
import net.sigmabeta.sage.ui.StringProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach

class OfflineUpdatesViewModelBrain(
    private val offlineRepository: OfflineRepository,
    private val threeTenTime: ThreeTenTime,
    stringProvider: StringProvider,
    hatchet: Hatchet,
    analytics: Analytics,
    scheduler: VglsScheduler,
) : ListViewModelBrain(
    stringProvider,
    analytics,
    hatchet,
    scheduler,
) {
    override val screenIdentifier = AnalyticsScreen.OFFLINE_UPDATES

    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> fetchResults()
            is VglsAction.Resume -> return
            is VglsAction.Noop -> return
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
