package com.vgleadsheets.remaster.browse

import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.TagRepository
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.list.ListViewModelBrain
import net.sigmabeta.sage.list.SageScheduler
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

class BrowseViewModelBrain(
    private val tagRepository: TagRepository,
    private val analytics: Analytics,
    stringProvider: StringProvider,
    hatchet: Hatchet,
    scheduler: SageScheduler,
) : ListViewModelBrain(
    stringProvider,
    analytics,
    hatchet,
    scheduler,
) {
    override val screenIdentifier = VglsAnalyticsScreen.BROWSE

    override fun initialState() = State()

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> startLoading()
            is SageAction.Resume -> return
            is Action.DestinationClicked -> onDestinationClicked(action.destination)
        }
    }

    private fun startLoading() {
        collectDatePublishedId()
    }

    private fun collectDatePublishedId() {
        tagRepository.getIdOfPublishDateTagKey()
            .onEach(::onPublishDateIdLoaded)
            .runInBackground()
    }

    private fun onPublishDateIdLoaded(id: Long?) {
        updatePublishDateId(LCE.Content(id))
    }

    private fun updatePublishDateId(id: LCE<Long?>) {
        updateState {
            (it as State).copy(
                publishDateId = id
            )
        }
    }

    private fun onDestinationClicked(destination: String) {
        emitEvent(
            SageEvent.NavigateTo(
                destination,
                Destination.BROWSE.name
            )
        )
    }
}
