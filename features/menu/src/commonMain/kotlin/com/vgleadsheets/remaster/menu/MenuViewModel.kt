package com.vgleadsheets.remaster.menu

import androidx.lifecycle.ViewModel
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.offline.OfflineWorkScheduler
import com.vgleadsheets.repository.DbUpdater
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.repository.history.UserContentGenerator
import com.vgleadsheets.repository.history.UserContentMigrator
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreen
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.appinfo.AppInfo
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.settings.DebugSettingsManager
import net.sigmabeta.sage.settings.GeneralSettingsManager
import net.sigmabeta.sage.time.ThreeTenTime
import net.sigmabeta.sage.ui.StringProvider

@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
class MenuViewModel @Inject constructor(
    override val stringProvider: StringProvider,
    override val analytics: Analytics,
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
    private val dbUpdater: DbUpdater,
    private val songHistoryRepository: SongHistoryRepository,
    private val generalSettingsManager: GeneralSettingsManager,
    private val debugSettingsManager: DebugSettingsManager,
    private val userContentGenerator: UserContentGenerator,
    private val userContentMigrator: UserContentMigrator,
    private val appInfo: AppInfo,
    private val threeTenTime: ThreeTenTime,
    private val offlineWorkScheduler: OfflineWorkScheduler,
) : VglsListViewModel<State>() {
    override val screenIdentifier = AnalyticsScreen.SETTINGS

    override fun initialState() = State()

    init {
        sendAction(SageAction.InitNoArgs)
    }

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> fetchSettings()
            is SageAction.Resume -> return
            is SageAction.Noop -> return
            is Action.CheckUpdatesClicked -> onCheckUpdatesClicked()
            is Action.ClearUsageClicked -> onClearUsageClicked()
            is Action.ClearSheetsClicked -> onClearSheetsClicked()
            is Action.KeepScreenOnClicked -> onKeepScreenOnClicked()
            is Action.WebsiteLinkClicked -> onWebsiteLinkClicked()
            is Action.GiantBombClicked -> onGiantBombClicked()
            is Action.PrivacyLinkClicked -> onPrivacyLinkClicked()
            is Action.WhatsNewClicked -> onWhatsNewClicked()
            is Action.BuildDateClicked -> onBuildDateClicked()
            is Action.LicensesLinkClicked -> onLicensesLinkClicked()
            is Action.FakeApiClicked -> onFakeApiClicked()
            is Action.DebugDelayClicked -> onDebugDelayClicked()
            is Action.DebugShowNavSnackbarsClicked -> onDebugShowNavSnackbarsClicked()
            is Action.DebugRenderOverlayClicked -> onDebugRenderOverlayClicked()
            is Action.GenerateUserContentClicked -> onGenerateUserContentClicked()
            is Action.GenerateUserContentLegacyClicked -> onGenerateUserContentLegacyClicked()
            is Action.MigrateUserContentLegacyClicked -> onMigrateUserContentLegacyClicked()
            is Action.RestartAppClicked -> onRestartAppClicked()
            is Action.RunOfflineDownloadClicked -> onRunOfflineDownloadClicked()
            is Action.OfflineUpdatesClicked -> onOfflineUpdatesClicked()
        }
    }

    private fun onCheckUpdatesClicked() {
        offlineWorkScheduler.scheduleDownload()
        updateState { it.copy(refreshCheckStatus = LCE.Content(Unit)) }
        emitEvent(
            SageEvent.ShowSnackbar(
                "Update job enqueued!",
                false,
                source = "DebugMenu"
            )
        )
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun onClearUsageClicked() {
        updateState { it.copy(usageDbClearStatus = LCE.Loading("clearUsage")) }

        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            try {
                songHistoryRepository.clearUsage()

                updateState { it.copy(usageDbClearStatus = LCE.Content(Unit)) }
                emitEvent(
                    SageEvent.ShowSnackbar(
                        "Usage history clear successful!",
                        false,
                        source = "DebugMenu"
                    )
                )
            } catch (error: Throwable) {
                showError("An error occurred clearing usage history.")
            }
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun onClearSheetsClicked() {
        updateState { it.copy(sheetDbClearStatus = LCE.Loading("clearSheets")) }

        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            try {
                dbUpdater.clearSheets()

                updateState { it.copy(sheetDbClearStatus = LCE.Content(Unit)) }
                emitEvent(
                    SageEvent.ShowSnackbar(
                        "Sheet database clear successful!",
                        false,
                        source = "DebugMenu"
                    )
                )
            } catch (error: Throwable) {
                showError("An error occurred clearing sheet database.")
            }
        }
    }

    private fun onKeepScreenOnClicked() {
        val oldValue = internalUiState.value.keepScreenOn ?: return
        generalSettingsManager.setKeepScreenOn(!oldValue)
    }

    private fun onLicensesLinkClicked() {
        navigateTo(Destination.LICENSES.noArgs())
    }

    private fun onWebsiteLinkClicked() {
        emitEvent(VglsEvent.WebsiteLinkClicked)
    }

    private fun onPrivacyLinkClicked() {
        emitEvent(VglsEvent.PrivacyLinkClicked)
    }

    private fun onGiantBombClicked() {
        emitEvent(VglsEvent.GiantBombLinkClicked)
    }

    private fun onWhatsNewClicked() {
        navigateTo(Destination.UPDATES.noArgs())
    }

    private fun onOfflineUpdatesClicked() {
        navigateTo(Destination.OFFLINE_UPDATES.noArgs())
    }

    @Suppress("MagicNumber")
    private fun onBuildDateClicked() {
        val oldValue = internalUiState.value.debugClickCount
        val newValue = oldValue + 1
        updateState { it.copy(debugClickCount = newValue) }
        if (newValue < 5) {
            return
        }

        toggleShouldShowDebug()
    }

    private fun toggleShouldShowDebug() {
        val oldValue = internalUiState.value.shouldShowDebug ?: return
        val newValue = !oldValue
        debugSettingsManager.setShouldShowDebug(newValue)
    }

    private fun onFakeApiClicked() {
        val oldValue = internalUiState.value.debugShouldUseFakeApi ?: return
        updateState { it.copy(debugShouldUseFakeApi = null) }
        debugSettingsManager.setShouldUseFakeApi(!oldValue)
    }

    private fun onDebugDelayClicked() {
        val oldValue = internalUiState.value.debugShouldDelay ?: return
        updateState { it.copy(debugShouldDelay = null) }
        debugSettingsManager.setShouldDelay(!oldValue)
    }

    private fun onDebugShowNavSnackbarsClicked() {
        val oldValue = internalUiState.value.debugShouldShowNavSnackbars ?: return
        updateState { it.copy(debugShouldShowNavSnackbars = null) }
        debugSettingsManager.setShouldShowSnackbars(!oldValue)
    }

    private fun onDebugRenderOverlayClicked() {
        val oldValue = internalUiState.value.debugShouldShowRenderOverlay ?: return
        updateState { it.copy(debugShouldShowRenderOverlay = null) }
        debugSettingsManager.setShouldShowRenderOverlay(!oldValue)
    }

    private fun fetchSettings() {
        fetchKeepScreenOn()
        fetchAppInfo()
        fetchShouldShowDebug()
        fetchDebugShouldUseFakeApi()
        fetchDebugShouldDelay()
        fetchDebugShouldShowNavSnackbars()
        fetchDebugShouldShowRenderOverlay()
    }

    private fun fetchAppInfo() {
        updateState { it.copy(appInfo = null) }
        flow { emit(Unit) }
            .onEach { _ ->
                updateState {
                    it.copy(
                        appInfo = appInfo,
                        formattedBuildDate = threeTenTime.longDateTextFromMillis(appInfo.buildTimeMs ?: 0)
                    )
                }
            }
            .runInBackground()
    }

    private fun fetchKeepScreenOn() {
        updateState { it.copy(keepScreenOn = null) }
        generalSettingsManager
            .getKeepScreenOn()
            .onEach { value ->
                updateState { it.copy(keepScreenOn = value) }
            }
            .runInBackground()
    }

    private fun fetchShouldShowDebug() {
        updateState { it.copy(shouldShowDebug = null) }
        debugSettingsManager
            .getShouldShowDebug()
            .onEach { value ->
                updateState { it.copy(shouldShowDebug = value) }
            }
            .runInBackground()
    }

    private fun fetchDebugShouldUseFakeApi() {
        updateState { it.copy(debugShouldUseFakeApi = null) }
        debugSettingsManager
            .getShouldUseFakeApi()
            .onEach { value ->
                updateState { it.copy(debugShouldUseFakeApi = value) }
            }
            .runInBackground()
    }

    private fun fetchDebugShouldDelay() {
        updateState { it.copy(debugShouldDelay = null) }
        debugSettingsManager
            .getShouldDelay()
            .onEach { value ->
                updateState { it.copy(debugShouldDelay = value) }
            }
            .runInBackground()
    }

    private fun fetchDebugShouldShowNavSnackbars() {
        updateState { it.copy(debugShouldShowNavSnackbars = null) }
        debugSettingsManager
            .getShouldShowSnackbars()
            .onEach { value ->
                updateState { it.copy(debugShouldShowNavSnackbars = value) }
            }
            .runInBackground()
    }

    private fun fetchDebugShouldShowRenderOverlay() {
        updateState { it.copy(debugShouldShowRenderOverlay = null) }
        debugSettingsManager
            .getShouldShowRenderOverlay()
            .onEach { value ->
                updateState { it.copy(debugShouldShowRenderOverlay = value) }
            }
            .runInBackground()
    }

    private fun onGenerateUserContentClicked() {
        updateState { it.copy(songRecordsGenerated = null) }
        userContentGenerator
            .generateRandomUserData()
            .onEach { songsAdded ->
                emitEvent(
                    SageEvent.ShowSnackbar(
                        "Added $songsAdded songs.",
                        false,
                        source = "DebugMenu"
                    )
                )
                updateState { it.copy(songRecordsGenerated = songsAdded) }
            }
            .runInBackground()
    }

    private fun onGenerateUserContentLegacyClicked() {
        updateState { it.copy(songRecordsGeneratedLegacy = null) }
        userContentGenerator
            .generateRandomUserDataLegacy()
            .onEach { songsAdded ->
                emitEvent(
                    SageEvent.ShowSnackbar(
                        "Added $songsAdded songs to legacy data.",
                        false,
                        source = "DebugMenu"
                    )
                )
                updateState { it.copy(songRecordsGeneratedLegacy = songsAdded) }
            }
            .runInBackground()
    }

    private fun onMigrateUserContentLegacyClicked() {
        updateState { it.copy(songRecordsMigrated = null) }
        userContentMigrator
            .migrateUserData()
            .onEach { songsAdded ->
                emitEvent(
                    SageEvent.ShowSnackbar(
                        "Migrated $songsAdded songs from legacy data.",
                        false,
                        source = "DebugMenu"
                    )
                )
                updateState { it.copy(songRecordsMigrated = songsAdded) }
            }
            .runInBackground()
    }

    private fun showError(message: String) {
        hatchet.e("Error occurred: $message")
        emitEvent(
            SageEvent.ShowSnackbar(
                message = "An error occurred. Try again after an app update.",
                withDismissAction = false,
                actionDetails = null,
                source = Destination.HOME.destName
            )
        )
    }

    private fun onRunOfflineDownloadClicked() {
        updateState { it.copy(offlineDownloadStatus = LCE.Loading("offlineDownload")) }
        offlineWorkScheduler.scheduleDownload()
        updateState { it.copy(offlineDownloadStatus = LCE.Content(Unit)) }
        emitEvent(
            SageEvent.ShowSnackbar(
                "Offline download job enqueued!",
                false,
                source = "DebugMenu"
            )
        )
    }

    private fun onRestartAppClicked() {
        emitEvent(
            VglsEvent.RestartApp
        )
    }

    private fun navigateTo(destinationString: String) {
        emitEvent(
            SageEvent.NavigateTo(
                destinationString,
                Destination.MENU.destName
            )
        )
    }
}
