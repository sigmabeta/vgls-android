package com.vgleadsheets.remaster.tags.values

import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

@AssistedInject
class TagValuesViewModel(
    @Assisted private val idArg: Long,
    override val stringProvider: StringProvider,
    override val analytics: Analytics,
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
    private val tagRepository: TagRepository,
) : VglsListViewModel<State>() {
    override val screenIdentifier = VglsAnalyticsScreen.LIST_TAG_VALUE

    override fun initialState() = State()

    init {
        sendAction(SageAction.InitWithId(idArg))
    }

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitWithId -> startLoading(action.id)
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
            SageEvent.NavigateTo(
                Destination.TAGS_VALUES_SONG_LIST.forId(id),
                Destination.TAGS_VALUES_LIST.name
            )
        )
    }

    private fun updateTagKey(tagKey: LCE<TagKey>) {
        updateState {
            it.copy(
                tagKey = tagKey
            )
        }
    }

    private fun updateTagValues(tagValues: LCE<List<TagValue>>) {
        updateState {
            it.copy(
                tagValues = tagValues
            )
        }
    }

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(@Assisted idArg: Long): TagValuesViewModel
    }

    companion object {
        private const val LOAD_OPERATION_KEY = "key.title"
        private const val LOAD_OPERATION_VALUES = "values.list"
    }
}
