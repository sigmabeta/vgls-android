package com.vgleadsheets.remaster.tags.list

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TagListViewModelBrain(
    private val tagRepository: TagRepository,
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
            is VglsAction.InitNoArgs -> startLoading()
            is Action.TagKeyClicked -> onTagClicked(action.id)
        }
    }

    private fun startLoading() {
        collectSongs()
    }

    private fun collectSongs() {
        tagRepository.getAllTagKeys()
            .onEach(::onTagKeysLoaded)
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun onTagKeysLoaded(tagKeys: List<TagKey>) {
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
}
