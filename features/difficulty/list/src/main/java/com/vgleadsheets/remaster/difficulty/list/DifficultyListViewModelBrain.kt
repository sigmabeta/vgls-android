package com.vgleadsheets.remaster.difficulty.list

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

class DifficultyListViewModelBrain(
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
    override val screenIdentifier = AnalyticsScreen.LIST_DIFFICULTY_TYPES

    override fun initialState() = State()

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> startLoading()
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
            SageEvent.NavigateTo(
                Destination.DIFFICULTY_VALUES_LIST.forId(id),
                Destination.DIFFICULTY_LIST.name
            )
        )
    }

    companion object {
        private const val LOAD_OPERATION_NAME = "difficulties.list"
    }
}
