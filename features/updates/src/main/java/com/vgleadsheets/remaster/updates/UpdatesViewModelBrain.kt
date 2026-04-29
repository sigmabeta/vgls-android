package com.vgleadsheets.remaster.updates

import com.vgleadsheets.analytics.Analytics
import com.vgleadsheets.analytics.AnalyticsScreen
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.model.updates.AppUpdate
import com.vgleadsheets.ui.StringProvider

class UpdatesViewModelBrain(
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
        when (action) {
            is VglsAction.InitNoArgs -> fetchUpdates()
            is VglsAction.Resume -> return
            is VglsAction.Noop -> return
        }
    }

    private fun fetchUpdates() {
        updateState {
            (it as State).copy(
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
