package com.vgleadsheets.remaster.composers.list

import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.model.Composer
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.ComposerRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.list.ListViewModelBrain
import net.sigmabeta.sage.list.SageScheduler
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

class ComposerListViewModelBrain(
    private val composerRepository: ComposerRepository,
    private val scheduler: SageScheduler,
    private val analytics: Analytics,
    stringProvider: StringProvider,
    hatchet: Hatchet,
) : ListViewModelBrain(
    stringProvider,
    analytics,
    hatchet,
    scheduler,
) {
    override val screenIdentifier = VglsAnalyticsScreen.LIST_COMPOSER

    override fun initialState() = State()

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> startLoading()
            is Action.ComposerClicked -> onComposerClicked(action.id)
        }
    }

    private fun startLoading() {
        showLoading()
        collectComposers()
    }

    private fun collectComposers() {
        composerRepository.getAllComposers()
            .onEach(::onComposersLoaded)
            .catch { error -> showError(LOAD_OPERATION_NAME, error) }
            .runInBackground()
    }

    private fun onComposersLoaded(composers: List<Composer>) {
        updateComposers(LCE.Content(composers))
    }

    private fun showLoading() {
        updateComposers(LCE.Loading(LOAD_OPERATION_NAME))
    }

    private fun showError(loadOperationName: String, error: Throwable) {
        updateComposers(LCE.Error(loadOperationName, error))
    }

    private fun updateComposers(composers: LCE<List<Composer>>) {
        updateState {
            (it as State).copy(
                composers = composers
            )
        }
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.COMPOSER_DETAIL.forId(id),
                Destination.COMPOSERS_LIST.name
            )
        )
    }

    companion object {
        private const val LOAD_OPERATION_NAME = "composers.list"
    }
}
