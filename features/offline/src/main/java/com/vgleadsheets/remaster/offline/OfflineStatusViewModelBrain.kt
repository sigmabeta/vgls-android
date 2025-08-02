package com.vgleadsheets.remaster.offline

import com.vgleadsheets.analytics.Analytics
import com.vgleadsheets.analytics.AnalyticsScreen
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.ui.StringProvider

class OfflineStatusViewModelBrain(
    stringProvider: StringProvider,
    hatchet: Hatchet,
    private val analytics: Analytics,
    private val scheduler: VglsScheduler,
) : ListViewModelBrain(
    stringProvider,
    analytics,
    hatchet,
    scheduler,
) {
    override val screenIdentifier = AnalyticsScreen.UPDATES

    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
    }
}
