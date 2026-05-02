package com.vgleadsheets.remaster.tags.list

import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.repository.TagRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreen
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.list.ListViewModelBrain
import net.sigmabeta.sage.list.VglsScheduler
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.nav.Destination
import net.sigmabeta.sage.ui.StringProvider

class TagListViewModelBrain(
    private val tagRepository: TagRepository,
    private val scheduler: VglsScheduler,
    private val analytics: Analytics,
    stringProvider: StringProvider,
    hatchet: Hatchet,
) : ListViewModelBrain(
    stringProvider,
    analytics,
    hatchet,
    scheduler,
) {
    override val screenIdentifier = AnalyticsScreen.LIST_TAG_KEY

    override fun initialState() = State()

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
            (it as State).copy(
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
