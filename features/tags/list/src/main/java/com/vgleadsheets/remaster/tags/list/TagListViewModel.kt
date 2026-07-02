package com.vgleadsheets.remaster.tags.list

import androidx.lifecycle.ViewModel
import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
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

@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
class TagListViewModel @Inject constructor(
    override val stringProvider: StringProvider,
    override val analytics: Analytics,
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
    private val tagRepository: TagRepository,
) : VglsListViewModel<State>() {
    override val screenIdentifier = VglsAnalyticsScreen.LIST_TAG_KEY

    override fun initialState() = State()

    init {
        sendAction(SageAction.InitNoArgs)
    }

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> startLoading()
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
            it.copy(
                tagKeys = tagKeys
            )
        }
    }

    private fun onTagClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.TAGS_VALUES_LIST.forId(id),
                Destination.TAGS_LIST.name
            )
        )
    }

    companion object {
        private const val LOAD_OPERATION_NAME = "tagkeys.list"
    }
}
