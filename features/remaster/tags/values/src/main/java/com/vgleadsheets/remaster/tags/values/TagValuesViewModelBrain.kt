package com.vgleadsheets.remaster.tags.values

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TagValuesViewModelBrain(
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
            is VglsAction.InitWithId -> startLoading(action.id)
            is Action.TagValueClicked -> onTagValueClicked(action.id)
        }
    }

    private fun startLoading(id: Long) {
        loadTagKey(id)
        loadTagValues(id)
    }

    private fun loadTagKey(id: Long) {
        tagRepository.getTagKey(id)
            .onEach(::onTagKeyLoaded)
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun loadTagValues(id: Long) {
        tagRepository.getTagValuesForTagKey(id)
            .onEach(::onTagValuesLoaded)
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun onTagKeyLoaded(tagKey: TagKey) {
        updateState {
            (it as State).copy(
                tagKey = tagKey
            )
        }
    }

    private fun onTagValuesLoaded(tagValues: List<TagValue>) {
        updateState {
            (it as State).copy(
                tagValues = tagValues
            )
        }
    }

    private fun onTagValueClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.TAGS_VALUES_SONG_LIST.forId(id),
                Destination.TAGS_VALUES_LIST.name
            )
        )
    }
}