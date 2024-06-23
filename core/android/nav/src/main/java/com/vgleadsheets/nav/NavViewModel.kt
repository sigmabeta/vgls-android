package com.vgleadsheets.nav

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.Hacks
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    override val dispatchers: VglsDispatchers,
    override val hatchet: Hatchet,
    override val eventDispatcher: EventDispatcher,
    private val notifManager: NotifManager,
    private val updateManager: UpdateManager,
) : VglsViewModel<NavState>() {
    init {
        eventDispatcher.addEventSink(this)
    }

    lateinit var navController: NavController
    lateinit var snackbarScope: CoroutineScope
    lateinit var snackbarHostState: SnackbarHostState

    override fun initialState() = NavState()

    override fun handleAction(action: VglsAction) {
        viewModelScope.launch(dispatchers.main) {
            hatchet.d("${this.javaClass.simpleName} - Handling action: $action")
        }
    }

    override fun handleEvent(event: VglsEvent) {
        viewModelScope.launch(dispatchers.main) {
            hatchet.d("${this@NavViewModel.javaClass.simpleName} - Handling event: $event")
            when (event) {
                is VglsEvent.NavigateTo -> navigateTo(event.destination)
                is VglsEvent.NavigateBack -> navigateBack()
                is VglsEvent.ShowSnackbar -> launchSnackbar(event)
                is VglsEvent.HideUiChrome -> hideSystemUi()
                is VglsEvent.ShowUiChrome -> showSystemUi()
                is VglsEvent.ClearNotif -> clearNotif(event.id)
                is VglsEvent.RefreshDb -> refreshDb()
            }
        }
    }

    private fun clearNotif(notifId: Long) {
        notifManager.removeNotif(notifId)
    }

    private fun refreshDb() {
        updateManager.refresh()
    }

    private fun launchSnackbar(snackbarEvent: VglsEvent.ShowSnackbar) {
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

    @Suppress("SwallowedException")
    private fun navigateTo(destination: String) {
        try {
            hatchet.v("Navigating to $destination...")
            navController.navigate(destination)
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
        navController.popBackStack()
    }

    private fun showSystemUi() {
        hatchet.d("Showing system UI.")
        updateState {
            it.copy(visibility = SystemUiVisibility.VISIBLE)
        }
        emitEvent(VglsEvent.UiChromeBecameShown)
    }

    private fun hideSystemUi() {
        hatchet.d("Hiding system UI.")
        viewModelScope.launch {
            delay(Hacks.UI_VISIBILITY_ANIM_DELAY)
            updateState {
                it.copy(visibility = SystemUiVisibility.HIDDEN)
            }
            emitEvent(VglsEvent.UiChromeBecameHidden)
        }
    }
}
