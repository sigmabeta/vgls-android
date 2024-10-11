package com.vgleadsheets.nav

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.get
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.common.debug.ShowDebugProvider
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.settings.DebugSettingsManager
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    override val dispatchers: VglsDispatchers,
    override val delayManager: DelayManager,
    override val hatchet: Hatchet,
    override val eventDispatcher: EventDispatcher,
    private val notifManager: NotifManager,
    private val updateManager: UpdateManager,
    private val debugSettingsManager: DebugSettingsManager,
    override val showDebugProvider: ShowDebugProvider,
) : VglsViewModel<NavState>() {
    init {
        eventDispatcher.addEventSink(this)
        sendAction(VglsAction.InitNoArgs)
    }

    private val internalShowSnackbarState = MutableStateFlow(false)

    private var backstackWatchJob: Job? = null
    private var settingsWatchJob: Job? = null

    lateinit var navController: NavController
    lateinit var snackbarScope: CoroutineScope
    lateinit var snackbarHostState: SnackbarHostState
    lateinit var topBarExpander: () -> Unit

    private val activityEventChannel = Channel<ActivityEvent>()
    val activityEvents: ReceiveChannel<ActivityEvent> = activityEventChannel

    override fun initialState() = NavState()

    override fun handleAction(action: VglsAction) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.v("${this.javaClass.simpleName} - Handling action: $action")
        }
    }

    override fun handleEvent(event: VglsEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.v("${this@NavViewModel.javaClass.simpleName} - Handling event: $event")
            when (event) {
                is VglsEvent.NavigateTo -> navigateTo(event.destination)
                is VglsEvent.NavigateSingleTopLevel -> navigateToTopLevel(event.destination)
                is VglsEvent.NavigateBack -> navigateBack()
                is VglsEvent.ShowSnackbar -> showSnackbar(event)
                is VglsEvent.HideUiChrome -> hideSystemUi()
                is VglsEvent.ShowUiChrome -> showSystemUi()
                is VglsEvent.ClearNotif -> clearNotif(event.id)
                is VglsEvent.RefreshDb -> refreshDb()
                is VglsEvent.GiantBombLinkClicked -> launchWebsite(URL_GB_WEBSITE)
                is VglsEvent.SearchYoutubeClicked -> launchWebsite(getYoutubeSearchUrlForQuery(event.query))
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

    private fun startWatchingBackstack() {
        if (backstackWatchJob == null) {
            hatchet.i("Backstack watcher not initialized. Starting now...")

            backstackWatchJob = navController
                .navigatorProvider[ComposeNavigator::class]
                .backStack
                .onEach {
                    if (internalShowSnackbarState.value) {
                        printBackstackStatus(it)
                    }
                }
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
        notifManager.removeNotif(id = StringId.ERROR_DB_UPDATE.hashCode().toLong())
        notifManager.removeNotif(id = StringId.ERROR_API_UPDATE.hashCode().toLong())
    }

    private fun launchWebsite(url: String) {
        val launcher = Intent(Intent.ACTION_VIEW)
        launcher.data = Uri.parse(url)

        val event = ActivityEvent.LaunchIntent(launcher)
        viewModelScope.launch {
            activityEventChannel.send(event)
        }
    }

    private fun showSnackbar(snackbarEvent: VglsEvent.ShowSnackbar) {
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
                    sink.sendAction(VglsAction.SnackbarActionClicked(actionDetails.clickAction))
                }

                SnackbarResult.Dismissed -> {
                    sink.sendAction(VglsAction.SnackbarDismissed(actionDetails.clickAction))
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

    @Suppress("SwallowedException")
    private fun navigateInternal(destination: String, topLevel: Boolean) {
        try {
            if (navController.currentDestination?.route == destination) {
                hatchet.w("Destination $destination matches current location; ignoring navigation request.")
                return
            }

            if (internalShowSnackbarState.value) {
                val message = "Navigating to $destination"
                hatchet.v(message)
                showSnackbar(
                    VglsEvent.ShowSnackbar(message, false, source = "Navigation")
                )
            }

            startWatchingBackstack()
            setupSettingsCollection()
            topBarExpander()

            if (topLevel) {
                navController.navigate(destination) {
                     popUpTo(navController.graph.startDestinationId)
                     launchSingleTop = true
                 }
            } else {
                navController.navigate(destination)
            }
        } catch (ex: IllegalArgumentException) {
            sendEvent(
                VglsEvent.ShowSnackbar(
                    message = "Unimplemented screen: $destination",
                    withDismissAction = true,
                    source = "Navigation"
                )
            )
        }
    }

    private fun navigateBack() {
        topBarExpander()
        startWatchingBackstack()
        setupSettingsCollection()

        val oldRoute = navController.currentDestination?.route
        val success = navController.popBackStack()

        if (!success) {
            viewModelScope.launch { activityEventChannel.send(ActivityEvent.Finish) }
            return
        }

        if (internalShowSnackbarState.value) {
            val newRoute = navController.currentDestination?.route
            val message = "Popping stack from $oldRoute to $newRoute"
            showSnackbar(
                VglsEvent.ShowSnackbar(message, false, source = "Navigation")
            )
        }
    }

    private fun printBackstackStatus(navBackStackEntries: List<NavBackStackEntry>) {
        hatchet.d("Nav backstack updated.")
        navBackStackEntries.forEach { entry ->
            hatchet.v("Dest: ${entry.destination.route} State: ${entry.maxLifecycle} Args: ${entry.arguments}")
        }
    }

    private fun showSystemUi() {
        hatchet.d("Showing system UI.")
        updateState {
            it.copy(visibility = SystemUiVisibility.VISIBLE)
        }
        emitEvent(VglsEvent.SystemBarsBecameShown)
    }

    private fun hideSystemUi() {
        hatchet.d("Hiding system UI.")
        viewModelScope.launch {
            updateState {
                it.copy(visibility = SystemUiVisibility.HIDDEN)
            }
            emitEvent(VglsEvent.SystemBarsBecameHidden)
        }
    }

    companion object {
        private const val URL_VGLS_WEBSITE = "https://www.vgleadsheets.com/"
        private const val URL_GB_WEBSITE = "https://www.giantbomb.com/"
    }
}
