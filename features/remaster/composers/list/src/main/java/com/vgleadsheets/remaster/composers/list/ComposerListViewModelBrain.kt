package com.vgleadsheets.remaster.composers.list

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListAction
import com.vgleadsheets.list.ListEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.model.Composer
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.VglsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ComposerListViewModelBrain(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    ): ListViewModelBrain() {
    override fun initialState() = State()

    override fun handleAction(action: ListAction) {
        when (action) {
            is ListAction.InitNoArgs -> collectComposers()
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
        internalUiState.update {
            (it as State).copy(
                composers = composers
            )
        }
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(ListEvent.NavigateTo(Destination.COMPOSER_DETAIL.forId(id)))
    }
}
