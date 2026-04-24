package com.vgleadsheets.remaster.offline.updates

import com.vgleadsheets.analytics.Analytics
import com.vgleadsheets.analytics.AnalyticsScreen
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.repository.OfflineRepository
import com.vgleadsheets.time.ThreeTenTime
import com.vgleadsheets.ui.StringProvider
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
