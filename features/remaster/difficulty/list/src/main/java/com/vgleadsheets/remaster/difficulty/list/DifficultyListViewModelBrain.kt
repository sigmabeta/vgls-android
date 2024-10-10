package com.vgleadsheets.remaster.difficulty.list

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach

class DifficultyListViewModelBrain(
    private val tagRepository: TagRepository,
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
            is Action.DifficultyTypeClicked -> onDifficultyTypeClicked(action.id)
        }
    }

    private fun startLoading() {
        showLoading()
        collectDifficultyTypes()
    }

    private fun collectDifficultyTypes() {
        tagRepository.getDifficultyTagKeys()
            .onEach(::onDifficultyTypesLoaded)
            .catch { error -> showError(LOAD_OPERATION_NAME, error) }
            .runInBackground()
    }

    private fun onDifficultyTypesLoaded(difficultyTypes: List<TagKey>) {
        updateDifficultyTypes(LCE.Content(difficultyTypes))
    }

    private fun showLoading() {
        updateDifficultyTypes(LCE.Loading(LOAD_OPERATION_NAME))
    }

    private fun showError(loadOperationName: String, error: Throwable) {
        updateDifficultyTypes(LCE.Error(loadOperationName, error))
    }

    private fun updateDifficultyTypes(difficultyTypes: LCE<List<TagKey>>) {
        updateState {
            (it as State).copy(
                difficultyTypes = difficultyTypes
            )
        }
    }

    private fun onDifficultyTypeClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.DIFFICULTY_VALUES_LIST.forId(id),
                Destination.DIFFICULTY_LIST.name
            )
        )
    }

    companion object {
        private const val LOAD_OPERATION_NAME = "difficulties.list"
    }
}
