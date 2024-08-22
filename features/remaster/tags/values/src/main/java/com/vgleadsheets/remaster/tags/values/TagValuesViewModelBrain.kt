package com.vgleadsheets.remaster.tags.values

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

class TagValuesViewModelBrain(
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
            is Action.TagValueClicked -> onTagValueClicked(action.id)
        }
    }

    private fun startLoading(id: Long) {
        showLoading()
        loadTagKey(id)
        loadTagValues(id)
    }

    private fun loadTagKey(id: Long) {
        tagRepository.getTagKey(id)
            .onEach(::onTagKeyLoaded)
            .catch { error -> showTagKeyError(error) }
            .runInBackground()
    }

    private fun loadTagValues(id: Long) {
        tagRepository.getTagValuesForTagKey(id)
            .onEach(::onTagValuesLoaded)
            .catch { error -> showTagValuesError(error) }
            .runInBackground()
    }

    private fun onTagKeyLoaded(tagKey: TagKey) {
        updateTagKey(LCE.Content(tagKey))
    }

    private fun showLoading() {
        updateTagKey(LCE.Loading(LOAD_OPERATION_KEY))
        updateTagValues(LCE.Loading(LOAD_OPERATION_VALUES))
    }

    private fun showTagKeyError(error: Throwable) {
        updateTagKey(LCE.Error(LOAD_OPERATION_KEY, error))
    }

    private fun showTagValuesError(error: Throwable) {
        updateTagValues(LCE.Error(LOAD_OPERATION_VALUES, error))
    }

    private fun onTagValuesLoaded(tagValues: List<TagValue>) {
        updateTagValues(LCE.Content(tagValues))
    }

    private fun onTagValueClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.TAGS_VALUES_SONG_LIST.forId(id),
                Destination.TAGS_VALUES_LIST.name
            )
        )
    }

    private fun updateTagKey(tagKey: LCE<TagKey>) {
        updateState {
            (it as State).copy(
                tagKey = tagKey
            )
        }
    }

    private fun updateTagValues(tagValues: LCE<List<TagValue>>) {
        updateState {
            (it as State).copy(
                tagValues = tagValues
            )
        }
    }

    companion object {
        private const val LOAD_OPERATION_KEY = "songs.list"
        private const val LOAD_OPERATION_VALUES = "songs.list"
    }
}
