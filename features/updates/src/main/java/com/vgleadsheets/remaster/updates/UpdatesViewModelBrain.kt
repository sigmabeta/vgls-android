package com.vgleadsheets.remaster.updates
import com.vgleadsheets.analytics.VglsAnalyticsScreen

import com.vgleadsheets.model.updates.AppUpdate
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreen
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.list.ListViewModelBrain
import net.sigmabeta.sage.list.SageScheduler
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

class UpdatesViewModelBrain(
    stringProvider: StringProvider,
    hatchet: Hatchet,
    private val analytics: Analytics,
    private val scheduler: SageScheduler,
) : ListViewModelBrain(
    stringProvider,
    analytics,
    hatchet,
    scheduler,
) {
    override val screenIdentifier = VglsAnalyticsScreen.UPDATES

    override fun initialState() = State()

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> fetchUpdates()
            is SageAction.Resume -> return
            is SageAction.Noop -> return
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
