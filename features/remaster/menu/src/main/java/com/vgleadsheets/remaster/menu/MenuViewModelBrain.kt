package com.vgleadsheets.remaster.menu

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.settings.DebugSettingsManager
import com.vgleadsheets.settings.GeneralSettingsManager
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MenuViewModelBrain(
    private val generalSettingsManager: GeneralSettingsManager,
    private val debugSettingsManager: DebugSettingsManager,
    stringProvider: StringProvider,
    hatchet: Hatchet,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    dispatchers,
    coroutineScope
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> fetchSettings()
            is VglsAction.Resume -> return
            is Action.KeepScreenOnClicked -> onKeepScreenOnClicked()
            is Action.WebsiteLinkClicked -> onWebsiteLinkClicked()
            is Action.GiantBombClicked -> onGiantBombClicked()
            is Action.LicensesLinkClicked -> onLicensesLinkClicked()
            is Action.DebugDelayClicked -> onDebugDelayClicked()
            is Action.RestartAppClicked -> onRestartAppClicked()
            else -> throw IllegalArgumentException("Invalid action for this screen.")
        }
    }

    private fun onKeepScreenOnClicked() {
        val oldValue = (internalUiState.value as State).keepScreenOn ?: return
        generalSettingsManager.setKeepScreenOn(!oldValue)
    }

    private fun onLicensesLinkClicked() {
        navigateTo(Destination.LICENSES.noArgs())
    }

    private fun onWebsiteLinkClicked() {
        emitEvent(VglsEvent.WebsiteLinkClicked)
    }

    private fun onGiantBombClicked() {
        emitEvent(VglsEvent.GiantBombLinkClicked)
    }

    private fun onDebugDelayClicked() {
        val oldValue = (internalUiState.value as State).debugShouldDelay ?: return
        debugSettingsManager.setShouldDelay(!oldValue)
    }

    private fun fetchSettings() {
        fetchKeepScreenOn()

        fetchDebugShouldDelay()
    }

    private fun fetchKeepScreenOn() {
        generalSettingsManager
            .getKeepScreenOn()
            .onEach { value ->
                updateState { (it as State).copy(keepScreenOn = value) }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun fetchDebugShouldDelay() {
        debugSettingsManager
            .getShouldDelay()
            .onEach { value ->
                updateState { (it as State).copy(debugShouldDelay = value ?: false) }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun onRestartAppClicked() {
        emitEvent(
            VglsEvent.RestartApp
        )
    }

    private fun navigateTo(destinationString: String) {
        emitEvent(
            VglsEvent.NavigateTo(
                destinationString,
                Destination.MENU.destName
            )
        )
    }
}