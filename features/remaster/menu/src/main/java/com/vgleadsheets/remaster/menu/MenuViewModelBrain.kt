package com.vgleadsheets.remaster.menu

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.appinfo.AppInfo
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.history.UserContentGenerator
import com.vgleadsheets.repository.history.UserContentMigrator
import com.vgleadsheets.settings.DebugSettingsManager
import com.vgleadsheets.settings.GeneralSettingsManager
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class MenuViewModelBrain(
    private val generalSettingsManager: GeneralSettingsManager,
    private val debugSettingsManager: DebugSettingsManager,
    private val userContentGenerator: UserContentGenerator,
    private val userContentMigrator: UserContentMigrator,
    private val appInfo: AppInfo,
    stringProvider: StringProvider,
    hatchet: Hatchet,
    private val scheduler: VglsScheduler,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    scheduler,
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> fetchSettings()
            is VglsAction.Resume -> return
            is VglsAction.Noop -> return
            is Action.KeepScreenOnClicked -> onKeepScreenOnClicked()
            is Action.WebsiteLinkClicked -> onWebsiteLinkClicked()
            is Action.GiantBombClicked -> onGiantBombClicked()
            is Action.WhatsNewClicked -> onWhatsNewClicked()
            is Action.BuildDateClicked -> onBuildDateClicked()
            is Action.LicensesLinkClicked -> onLicensesLinkClicked()
            is Action.DebugDelayClicked -> onDebugDelayClicked()
            is Action.DebugShowNavSnackbarsClicked -> onDebugShowNavSnackbarsClicked()
            is Action.GenerateUserContentClicked -> onGenerateUserContentClicked()
            is Action.GenerateUserContentLegacyClicked -> onGenerateUserContentLegacyClicked()
            is Action.MigrateUserContentLegacyClicked -> onMigrateUserContentLegacyClicked()
            is Action.RestartAppClicked -> onRestartAppClicked()
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

    private fun onWhatsNewClicked() {
        navigateTo(Destination.UPDATES.noArgs())
    }

    @Suppress("MagicNumber")
    private fun onBuildDateClicked() {
        val oldValue = (internalUiState.value as State).debugClickCount
        val newValue = oldValue + 1
        updateState { (it as State).copy(debugClickCount = newValue) }
        if (newValue < 5) {
            return
        }

        toggleShouldShowDebug()
    }

    private fun toggleShouldShowDebug() {
        val oldValue = (internalUiState.value as State).shouldShowDebug ?: return
        val newValue = !oldValue
        debugSettingsManager.setShouldShowDebug(newValue)
    }

    private fun onDebugDelayClicked() {
        val oldValue = (internalUiState.value as State).debugShouldDelay ?: return
        updateState { (it as State).copy(debugShouldDelay = null) }
        debugSettingsManager.setShouldDelay(!oldValue)
    }

    private fun onDebugShowNavSnackbarsClicked() {
        val oldValue = (internalUiState.value as State).debugShouldShowNavSnackbars ?: return
        updateState { (it as State).copy(debugShouldShowNavSnackbars = null) }
        debugSettingsManager.setShouldShowSnackbars(!oldValue)
    }

    private fun fetchSettings() {
        fetchKeepScreenOn()
        fetchAppInfo()
        fetchShouldShowDebug()
        fetchDebugShouldDelay()
        fetchDebugShouldShowNavSnackbars()
    }

    private fun fetchAppInfo() {
        updateState { (it as State).copy(appInfo = null) }
        flow { emit(Unit) }
            .onEach { _ ->
                updateState { (it as State).copy(appInfo = appInfo) }
            }
            .runInBackground()
    }

    private fun fetchKeepScreenOn() {
        updateState { (it as State).copy(keepScreenOn = null) }
        generalSettingsManager
            .getKeepScreenOn()
            .onEach { value ->
                updateState { (it as State).copy(keepScreenOn = value) }
            }
            .runInBackground()
    }

    private fun fetchShouldShowDebug() {
        updateState { (it as State).copy(shouldShowDebug = null) }
        debugSettingsManager
            .getShouldShowDebug()
            .onEach { value ->
                updateState { (it as State).copy(shouldShowDebug = value) }
            }
            .runInBackground()
    }

    private fun fetchDebugShouldDelay() {
        updateState { (it as State).copy(debugShouldDelay = null) }
        debugSettingsManager
            .getShouldDelay()
            .onEach { value ->
                updateState { (it as State).copy(debugShouldDelay = value) }
            }
            .runInBackground()
    }

    private fun fetchDebugShouldShowNavSnackbars() {
        updateState { (it as State).copy(debugShouldShowNavSnackbars = null) }
        debugSettingsManager
            .getShouldShowSnackbars()
            .onEach { value ->
                updateState { (it as State).copy(debugShouldShowNavSnackbars = value) }
            }
            .runInBackground()
    }

    private fun onGenerateUserContentClicked() {
        updateState { (it as State).copy(songRecordsGenerated = null) }
        userContentGenerator
            .generateRandomUserData()
            .onEach { songsAdded ->
                emitEvent(
                    VglsEvent.ShowSnackbar(
                        "Added $songsAdded songs.",
                        false,
                        source = "DebugMenu"
                    )
                )
                updateState { (it as State).copy(songRecordsGenerated = songsAdded) }
            }
            .runInBackground()
    }

    private fun onGenerateUserContentLegacyClicked() {
        updateState { (it as State).copy(songRecordsGeneratedLegacy = null) }
        userContentGenerator
            .generateRandomUserDataLegacy()
            .onEach { songsAdded ->
                emitEvent(
                    VglsEvent.ShowSnackbar(
                        "Added $songsAdded songs to legacy data.",
                        false,
                        source = "DebugMenu"
                    )
                )
                updateState { (it as State).copy(songRecordsGeneratedLegacy = songsAdded) }
            }
            .runInBackground()
    }

    private fun onMigrateUserContentLegacyClicked() {
        updateState { (it as State).copy(songRecordsMigrated = null) }
        userContentMigrator
            .migrateUserData()
            .onEach { songsAdded ->
                emitEvent(
                    VglsEvent.ShowSnackbar(
                        "Migrated $songsAdded songs from legacy data.",
                        false,
                        source = "DebugMenu"
                    )
                )
                updateState { (it as State).copy(songRecordsMigrated = songsAdded) }
            }
            .runInBackground()
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
