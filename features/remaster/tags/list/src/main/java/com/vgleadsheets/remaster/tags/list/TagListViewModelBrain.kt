package com.vgleadsheets.remaster.tags.list

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

class TagListViewModelBrain(
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
            is Action.TagKeyClicked -> onTagClicked(action.id)
        }
    }

    private fun startLoading() {
        showLoading()
        collectTagKeys()
    }

    private fun collectTagKeys() {
        tagRepository.getDetailTagKeys()
            .onEach(::onTagKeysLoaded)
            .catch { error -> showError(LOAD_OPERATION_NAME, error) }
            .runInBackground()
    }

    private fun onTagKeysLoaded(tagKeys: List<TagKey>) {
        updateTagKeys(LCE.Content(tagKeys))
    }

    private fun showLoading() {
        updateTagKeys(LCE.Loading(LOAD_OPERATION_NAME))
    }

    private fun showError(loadOperationName: String, error: Throwable) {
        updateTagKeys(LCE.Error(loadOperationName, error))
    }

    private fun updateTagKeys(tagKeys: LCE<List<TagKey>>) {
        updateState {
            (it as State).copy(
                tagKeys = tagKeys
            )
        }
    }

    private fun onTagClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.TAGS_VALUES_LIST.forId(id),
                Destination.TAGS_LIST.name
            )
        )
    }

    companion object {
        private const val LOAD_OPERATION_NAME = "tagkeys.list"
    }
}
