package com.vgleadsheets.nav

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.strings.VglsStringId
import com.vgleadsheets.viewmodel.VglsViewModel
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.settings.DebugSettingsManager

@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
class NavViewModel @Inject constructor(
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val hatchet: Hatchet,
    override val analytics: Analytics,
    override val eventDispatcher: EventDispatcher,
    private val notifManager: NotifManager,
    private val updateManager: UpdateManager,
    private val debugSettingsManager: DebugSettingsManager,
    override val showDebugProvider: ShowDebugProvider,
) : VglsViewModel<NavState>() {
    init {
        eventDispatcher.addEventSink(this)
        sendAction(SageAction.InitNoArgs)
    }

    private val internalShowSnackbarState = MutableStateFlow(false)

    private var settingsWatchJob: Job? = null

    /** The Voyager root Navigator + the route→Screen mapper, both set by RemasterAppUi (replaces the
     * old AndroidX NavController). Nullable because they're assigned after this VM is constructed. */
    var navigator: Navigator? = null
    var screenForRoute: ((String) -> Screen)? = null
    lateinit var snackbarScope: CoroutineScope
    lateinit var snackbarHostState: SnackbarHostState
    lateinit var topBarExpander: () -> Unit

    private val activityEventChannel = Channel<ActivityEvent>()
    val activityEvents: ReceiveChannel<ActivityEvent> = activityEventChannel

    override val screenIdentifier = null

    override fun initialState() = NavState()

    override fun sendInitAction() = Unit

    override fun handleAction(action: SageAction) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.v("${this.javaClass.simpleName} - Handling action: $action")
        }
    }

    override fun handleEvent(event: SageEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.v("${this@NavViewModel.javaClass.simpleName} - Handling event: $event")
            when (event) {
                is SageEvent.NavigateTo -> navigateTo(event.destination)
                is SageEvent.NavigateSingleTopLevel -> navigateToTopLevel(event.destination)
                is SageEvent.NavigateBack -> navigateBack()
                is SageEvent.ShowSnackbar -> showSnackbar(event)
                is SageEvent.HideUiChrome -> hideSystemUi()
                is SageEvent.ShowUiChrome -> showSystemUi()
                is SageEvent.ClearNotif -> clearNotif(event.id)
                is VglsEvent.RefreshDb -> refreshDb()
                is VglsEvent.GiantBombLinkClicked -> launchWebsite(URL_GB_WEBSITE)
                is VglsEvent.SearchYoutubeClicked -> launchWebsite(getYoutubeSearchUrlForQuery(event.query))
                is VglsEvent.PrivacyLinkClicked -> launchWebsite(URL_PRIVACY)
                is VglsEvent.WebsiteLinkClicked -> launchWebsite(URL_VGLS_WEBSITE)
                is VglsEvent.RestartApp -> restartApp()
            }
        }
    }

    private fun setupSettingsCollection() {
        if (settingsWatchJob == null) {
            hatchet.i("Settings watcher not initialized. Starting now...")
            settingsWatchJob = debugSettingsManager.getShouldShowSnackbars()
                .map { it }
                .onEach { shouldShow ->
                    internalShowSnackbarState.update {
                        shouldShow
                    }
                }
                .flowOn(dispatchers.disk)
                .launchIn(viewModelScope)
        }
    }

    private fun restartApp() {
        viewModelScope.launch {
            activityEventChannel.send(ActivityEvent.Restart)
        }
    }

    private fun clearNotif(notifId: Long) {
        notifManager.removeNotif(notifId)
    }

    private fun refreshDb() {
        updateManager.refresh()
        notifManager.removeNotif(id = VglsStringId.ERROR_DB_UPDATE.hashCode().toLong())
        notifManager.removeNotif(id = VglsStringId.ERROR_API_UPDATE.hashCode().toLong())
    }

    private fun launchWebsite(url: String) {
        val launcher = Intent(Intent.ACTION_VIEW)
        launcher.data = Uri.parse(url)

        val event = ActivityEvent.LaunchIntent(launcher)
        viewModelScope.launch {
            activityEventChannel.send(event)
        }
    }

    private fun showSnackbar(snackbarEvent: SageEvent.ShowSnackbar) {
        snackbarScope.launch {
            val actionDetails = snackbarEvent.actionDetails
            val result = snackbarHostState.showSnackbar(
                message = snackbarEvent.message,
                actionLabel = actionDetails?.clickActionLabel,
                withDismissAction = snackbarEvent.withDismissAction,
                duration = SnackbarDuration.Short
            )

            if (actionDetails == null) {
                return@launch
            }

            val sink = actionDetails.actionSink

            when (result) {
                SnackbarResult.ActionPerformed -> {
                    sink.sendAction(SageAction.SnackbarActionClicked(actionDetails.clickAction))
                }

                SnackbarResult.Dismissed -> {
                    sink.sendAction(SageAction.SnackbarDismissed(actionDetails.clickAction))
                }
            }
        }
    }

    private fun navigateTo(destination: String) {
        navigateInternal(destination, false)
    }

    private fun navigateToTopLevel(destination: String) {
        navigateInternal(destination, true)
    }

    private fun currentRoute(): String? = (navigator?.lastItem as? RoutedScreen)?.route

    @Suppress("SwallowedException", "ReturnCount")
    private fun navigateInternal(destination: String, topLevel: Boolean) {
        val navigator = this.navigator ?: return
        val screenFor = this.screenForRoute ?: return
        try {
            if (currentRoute() == destination) {
                hatchet.w("Destination $destination matches current location; ignoring navigation request.")
                return
            }

            if (internalShowSnackbarState.value) {
                val message = "Navigating to $destination"
                hatchet.v(message)
                showSnackbar(
                    SageEvent.ShowSnackbar(message, false, source = "Navigation")
                )
            }

            setupSettingsCollection()
            topBarExpander()

            if (topLevel) {
                // Reset to the home root then the tab target (matches the old popUpTo(start) +
                // launchSingleTop: back stack becomes [Home] or [Home, target]).
                val home = Destination.HOME.noArgs()
                val screens = if (destination == home) {
                    listOf(screenFor(home))
                } else {
                    listOf(screenFor(home), screenFor(destination))
                }
                navigator.replaceAll(screens)
            } else {
                navigator.push(screenFor(destination))
            }

            eventDispatcher.sendEvent(SageEvent.NavigateSuccessTo(destination))
        } catch (ex: IllegalStateException) {
            sendEvent(
                SageEvent.ShowSnackbar(
                    message = "Unimplemented screen: $destination",
                    withDismissAction = true,
                    source = "Navigation"
                )
            )
        }
    }

    private fun navigateBack() {
        topBarExpander()
        setupSettingsCollection()

        val navigator = this.navigator
        val oldRoute = currentRoute()
        val success = navigator?.pop() ?: false

        if (!success) {
            viewModelScope.launch { activityEventChannel.send(ActivityEvent.Finish) }
            return
        }

        val newRoute = currentRoute()
        if (newRoute != null) {
            eventDispatcher.sendEvent(SageEvent.NavigateSuccessTo(newRoute))
        }

        if (internalShowSnackbarState.value) {
            val message = "Popping stack from $oldRoute to $newRoute"
            showSnackbar(
                SageEvent.ShowSnackbar(message, false, source = "Navigation")
            )
        }
    }

    private fun showSystemUi() {
        if (internalUiState.value.visibility == SystemUiVisibility.VISIBLE) {
            return
        }

        hatchet.d("Showing system UI.")
        updateState {
            it.copy(visibility = SystemUiVisibility.VISIBLE)
        }
        emitEvent(SageEvent.SystemBarsBecameShown)
    }

    private fun hideSystemUi() {
        if (internalUiState.value.visibility == SystemUiVisibility.HIDDEN) {
            return
        }

        hatchet.d("Hiding system UI.")
        updateState {
            it.copy(visibility = SystemUiVisibility.HIDDEN)
        }
        emitEvent(SageEvent.SystemBarsBecameHidden)
    }

    companion object {
        private const val URL_VGLS_WEBSITE = "https://www.vgleadsheets.com/"
        private const val URL_GB_WEBSITE = "https://www.giantbomb.com/"
        private const val URL_PRIVACY = "https://github.com/sigmabeta/vgls-android/blob/beta/PRIVACY.md/"
    }
}
