package com.vgleadsheets.remaster.browse

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.onEach

class BrowseViewModelBrain(
    private val tagRepository: TagRepository,
    stringProvider: StringProvider,
    hatchet: Hatchet,
    scheduler: VglsScheduler,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    scheduler,
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> startLoading()
            is VglsAction.Resume -> return
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
            VglsEvent.NavigateTo(
                destination,
                Destination.BROWSE.name
            )
        )
    }
}
