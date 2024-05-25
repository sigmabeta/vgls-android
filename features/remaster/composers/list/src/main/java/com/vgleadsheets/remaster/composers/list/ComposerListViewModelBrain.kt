package com.vgleadsheets.remaster.composers.list

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Composer
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ComposerListViewModelBrain(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    stringProvider: StringProvider,
    hatchet: Hatchet,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    dispatchers,
    coroutineScope
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> collectComposers()
            is Action.ComposerClicked -> onComposerClicked(action.id)
        }
    }

    private fun collectComposers() {
        repository.getAllComposers()
            .onEach(::onComposersLoaded)
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun onComposersLoaded(composers: List<Composer>) {
        updateState {
            (it as State).copy(
                composers = composers
            )
        }
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(VglsEvent.NavigateTo(Destination.COMPOSER_DETAIL.forId(id)))
    }
}
