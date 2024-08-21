package com.vgleadsheets.remaster.composers.list

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Composer
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach

class ComposerListViewModelBrain(
    private val composerRepository: ComposerRepository,
    private val scheduler: VglsScheduler,
    stringProvider: StringProvider,
    hatchet: Hatchet,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    scheduler,
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> startLoading()
            is Action.ComposerClicked -> onComposerClicked(action.id)
        }
    }

    private fun startLoading() {
        showLoading()
        collectComposers()
    }

    private fun collectComposers() {
        composerRepository.getAllComposers()
            .onEach(::onComposersLoaded)
            .catch { error -> showError(LOAD_OPERATION_NAME, error) }
            .runInBackground()
    }

    private fun onComposersLoaded(composers: List<Composer>) {
        updateComposers(LCE.Content(composers))
    }

    private fun showLoading() {
        updateComposers(LCE.Loading(LOAD_OPERATION_NAME))
    }

    private fun showError(loadOperationName: String, error: Throwable) {
        updateComposers(LCE.Error(loadOperationName, error))
    }

    private fun updateComposers(composers: LCE<List<Composer>>) {
        updateState {
            (it as State).copy(
                composers = composers
            )
        }
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.COMPOSER_DETAIL.forId(id),
                Destination.COMPOSERS_LIST.name
            )
        )
    }

    companion object {
        private const val LOAD_OPERATION_NAME = "composers.list"
    }
}
