package com.vgleadsheets.remaster.difficulty.list

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
class DifficultyListViewModel @Inject constructor(
    override val stringProvider: StringProvider,
    override val analytics: Analytics,
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
    private val tagRepository: TagRepository,
) : VglsListViewModel<State>() {
    override val screenIdentifier = VglsAnalyticsScreen.LIST_DIFFICULTY_TYPES

    override fun initialState() = State()

    init {
        sendAction(SageAction.InitNoArgs)
    }

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
            it.copy(
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
