package com.vgleadsheets.remaster.difficulty.values

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach

class DifficultyValuesViewModelBrain(
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
            is VglsAction.InitWithId -> startLoading(action.id)
            is Action.DifficultyValueClicked -> onDifficultyValueClicked(action.id)
        }
    }

    private fun startLoading(id: Long) {
        showLoading()
        loadTagKey(id)
        loadDifficultyValues(id)
    }

    private fun loadTagKey(id: Long) {
        tagRepository.getTagKey(id)
            .onEach(::onTagKeyLoaded)
            .catch { error -> showTagKeyError(error) }
            .runInBackground()
    }

    private fun loadDifficultyValues(id: Long) {
        tagRepository.getTagValuesForTagKey(id)
            .onEach(::onDifficultyValuesLoaded)
            .catch { error -> showDifficultyValuesError(error) }
            .runInBackground()
    }

    private fun onTagKeyLoaded(tagKey: TagKey) {
        updateTagKey(LCE.Content(tagKey))
    }

    private fun showLoading() {
        updateTagKey(LCE.Loading(LOAD_OPERATION_KEY))
        updateDifficultyValues(LCE.Loading(LOAD_OPERATION_VALUES))
    }

    private fun showTagKeyError(error: Throwable) {
        updateTagKey(LCE.Error(LOAD_OPERATION_KEY, error))
    }

    private fun showDifficultyValuesError(error: Throwable) {
        updateDifficultyValues(LCE.Error(LOAD_OPERATION_VALUES, error))
    }

    private fun onDifficultyValuesLoaded(difficultyValues: List<TagValue>) {
        updateDifficultyValues(LCE.Content(difficultyValues))
    }

    private fun onDifficultyValueClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.TAGS_VALUES_SONG_LIST.forId(id),
                Destination.DIFFICULTY_VALUES_LIST.name
            )
        )
    }

    private fun updateTagKey(tagKey: LCE<TagKey>) {
        updateState {
            (it as State).copy(
                difficultyType = tagKey
            )
        }
    }

    private fun updateDifficultyValues(difficultyValues: LCE<List<TagValue>>) {
        updateState {
            (it as State).copy(
                difficultyValues = difficultyValues
            )
        }
    }

    companion object {
        private const val LOAD_OPERATION_KEY = "key.title"
        private const val LOAD_OPERATION_VALUES = "values.list"
    }
}
